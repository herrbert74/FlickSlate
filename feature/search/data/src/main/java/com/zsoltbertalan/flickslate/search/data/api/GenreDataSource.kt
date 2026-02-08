package com.zsoltbertalan.flickslate.search.data.api

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.shared.domain.model.Genre
import com.zsoltbertalan.flickslate.shared.domain.model.GenresReply
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
