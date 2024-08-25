package com.zsoltbertalan.flickslate.search.data.db

import com.zsoltbertalan.flickslate.domain.model.Genre
import kotlinx.coroutines.flow.Flow

interface GenreDataSource {

	suspend fun purgeDatabase()

	suspend fun insertGenres(genres: List<Genre>)

	suspend fun insertEtag(etag: String)

	suspend fun getEtag(): String

	fun getGenres(): Flow<List<Genre>>

	fun getGenre(id: String): Genre

}
