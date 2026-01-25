package com.zsoltbertalan.flickslate.movies.data.api

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome

internal interface MovieFavoritesDataSource {

	interface Remote {

		suspend fun setMovieFavorite(accountId: Int, sessionId: String, movieId: Int, favorite: Boolean): Outcome<Unit>

	}

}
