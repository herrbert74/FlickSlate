package com.zsoltbertalan.flickslate.search.data.api

import com.zsoltbertalan.flickslate.shared.model.Genre
import com.zsoltbertalan.flickslate.shared.model.GenresReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import kotlinx.coroutines.flow.Flow

interface GenreDataSource {

	interface Local {

		suspend fun purgeDatabase()

		suspend fun insertGenres(genres: List<Genre>)

		suspend fun insertEtag(etag: String)

		suspend fun getEtag(): String?

		fun getGenres(): Flow<GenresReply>

		fun getGenre(id: Int): Genre

	}

	interface Remote {

		suspend fun getGenres(etag: String): Outcome<GenresReply>

	}

}
