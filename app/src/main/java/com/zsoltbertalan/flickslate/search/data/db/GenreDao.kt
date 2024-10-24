package com.zsoltbertalan.flickslate.search.data.db

import com.zsoltbertalan.flickslate.search.data.api.GenreDataSource
import com.zsoltbertalan.flickslate.shared.async.IoDispatcher
import com.zsoltbertalan.flickslate.shared.domain.model.Genre
import com.zsoltbertalan.flickslate.search.data.db.model.EtagDbo
import com.zsoltbertalan.flickslate.search.data.db.model.GenreDbo
import com.zsoltbertalan.flickslate.search.data.db.model.toDbo
import com.zsoltbertalan.flickslate.search.data.db.model.toGenre
import com.zsoltbertalan.flickslate.shared.domain.model.GenresReply
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreDao @Inject constructor(
	private val realm: Realm,
	@IoDispatcher private val ioContext: CoroutineDispatcher,
) : GenreDataSource.Local {

	override suspend fun purgeDatabase() {
		realm.write {
			val genres = this.query(GenreDbo::class).find()
			delete(genres)
		}
	}

	override suspend fun insertGenres(genres: List<Genre>) {
		realm.write {
			genres.map { copyToRealm(it.toDbo(), UpdatePolicy.ALL) }
		}
	}

	override suspend fun insertEtag(etag: String) {
		realm.write {
			copyToRealm(EtagDbo("genres", etag = etag), UpdatePolicy.ALL)
		}
	}

	override suspend fun getEtag(): String = withContext(ioContext) {
		return@withContext realm.query<EtagDbo>("id = $0", "genres").first().find()?.etag ?: ""
	}

	override fun getGenres(): Flow<GenresReply> =
		realm.query(GenreDbo::class)
			.asFlow()
			.map { dbo ->
				val etag = getEtag()
				GenresReply(dbo.list.map { it.toGenre() }, etag)
			}.flowOn(ioContext)

	override fun getGenre(id: String): Genre {
		return realm.query<GenreDbo>("id = $0", id).first().find()?.toGenre() ?: Genre()
	}

}
