package com.zsoltbertalan.flickslate.search.data.db

import com.zsoltbertalan.flickslate.search.data.api.GenreDataSource
import com.zsoltbertalan.flickslate.search.data.db.model.EtagEntity
import com.zsoltbertalan.flickslate.search.data.db.model.toEntity
import com.zsoltbertalan.flickslate.search.data.db.model.toGenre
import com.zsoltbertalan.flickslate.shared.data.util.runCatchingUnit
import com.zsoltbertalan.flickslate.shared.domain.model.Genre
import com.zsoltbertalan.flickslate.shared.domain.model.GenresReply
import com.zsoltbertalan.flickslate.shared.kotlin.async.IoDispatcher
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ViewModelScoped
class GenresRoomDataSource @Inject constructor(
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
