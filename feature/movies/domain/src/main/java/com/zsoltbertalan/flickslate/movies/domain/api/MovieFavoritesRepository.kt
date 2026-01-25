package com.zsoltbertalan.flickslate.movies.domain.api

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome

interface MovieFavoritesRepository {

	suspend fun setMovieFavorite(accountId: Int, sessionId: String, movieId: Int, favorite: Boolean): Outcome<Unit>

}
