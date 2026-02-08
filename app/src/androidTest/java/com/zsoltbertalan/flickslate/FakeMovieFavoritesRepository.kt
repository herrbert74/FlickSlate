package com.zsoltbertalan.flickslate

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.movies.domain.api.MovieFavoritesRepository

class FakeMovieFavoritesRepository : MovieFavoritesRepository {

	override suspend fun setMovieFavorite(
		accountId: Int,
		sessionId: String,
		movieId: Int,
		favorite: Boolean
	): Outcome<Unit> {
		AccountListTestState.setMovieFavorite(movieId, favorite)
		return Ok(Unit)
	}
}
