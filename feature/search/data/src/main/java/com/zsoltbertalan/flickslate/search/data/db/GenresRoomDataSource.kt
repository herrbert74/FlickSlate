package com.zsoltbertalan.flickslate.search.data.db

import com.zsoltbertalan.flickslate.base.kotlin.async.IoDispatcher
import com.zsoltbertalan.flickslate.search.data.api.GenreDataSource
import com.zsoltbertalan.flickslate.search.data.db.model.EtagEntity
import com.zsoltbertalan.flickslate.search.data.db.model.toEntity
import com.zsoltbertalan.flickslate.search.data.db.model.toGenre
import com.zsoltbertalan.flickslate.shared.data.util.runCatchingUnit
import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import com.zsoltbertalan.flickslate.shared.domain.model.Genre
import com.zsoltbertalan.flickslate.shared.domain.model.GenresReply
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@ContributesBinding(ActivityRetainedScope::class)
@SingleIn(ActivityRetainedScope::class)
class GenresRoomDataSource @Inject internal constructor(
	private val searchDatabase: SearchDatabase,
	@param:IoDispatcher private val ioContext: CoroutineDispatcher,
) : GenreDataSource.Local {

	override suspend fun purgeDatabase() {
		runCatchingUnit {
			searchDatabase.genresDao().purgeDatabase()
		}
	}

	override suspend fun insertGenres(genres: List<Genre>) {
		withContext(ioContext) {
			runCatchingUnit {
				val m = genres.map { it.toEntity() }
				searchDatabase.genresDao().insertGenres(m)
			}
		}
	}

	override suspend fun insertEtag(etag: String) {
		withContext(ioContext) {
			runCatchingUnit {
				searchDatabase.etagDao().insertEtag(EtagEntity("genres", etag))
			}
		}
	}

	override fun getGenres(): Flow<GenresReply> =
		searchDatabase.genresDao().getAll()
			.distinctUntilChanged()
			.map { genreEntities ->
				val etag = getEtag()
				GenresReply(genreEntities.map { it.toGenre() }, etag)
			}.flowOn(ioContext)

	override fun getGenre(id: Int): Genre {
		return searchDatabase.genresDao().getGenre(id)?.toGenre() ?: Genre()
	}

	override suspend fun getEtag(): String? = withContext(ioContext) {
		return@withContext searchDatabase.etagDao().getAll().firstOrNull()?.etag
	}

}
