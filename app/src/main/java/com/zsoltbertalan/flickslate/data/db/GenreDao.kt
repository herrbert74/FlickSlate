package com.zsoltbertalan.flickslate.data.db

import com.zsoltbertalan.flickslate.domain.model.Genre
import com.zsoltbertalan.flickslate.ext.apiRunCatching
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GenreDao @Inject constructor(private val realm: Realm) : GenreDataSource {

	override suspend fun purgeDatabase() {
		realm.write {
			val genres = this.query(GenreDbo::class).find()
			delete(genres)
		}
	}

	override suspend fun insertGenres(genres: List<Genre>) {
		apiRunCatching {
			realm.write {
				genres.map { copyToRealm(it.toDbo(), UpdatePolicy.ALL) }
			}
		}
	}

	override suspend fun insertEtag(etag: String) {
		apiRunCatching {
			realm.write {
				copyToRealm(EtagDbo("genres", etag = etag), UpdatePolicy.ALL)
			}
		}
	}

	override suspend fun getEtag(): String {
		return realm.query<EtagDbo>("id = $0", "genres").first().find()?.etag ?: ""
	}

	override fun getGenres(): Flow<List<Genre>> {
		return realm.query(GenreDbo::class).asFlow().map { dbo -> dbo.list.map { it.toGenre() } }
	}

	override fun getGenre(id: String): Genre {
		return realm.query<GenreDbo>("id = $0", id).first().find()?.toGenre() ?: Genre()
	}

}
