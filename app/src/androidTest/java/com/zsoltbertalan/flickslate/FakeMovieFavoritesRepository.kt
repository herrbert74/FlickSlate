package com.zsoltbertalan.flickslate

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.movies.domain.api.MovieFavoritesRepository
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class FakeMovieFavoritesRepository @Inject constructor() : MovieFavoritesRepository {

	override suspend fun setMovieFavorite(
		accountId: Int,
		sessionId: String,
		movieId: Int,
		favorite: Boolean
	): Outcome<Unit> = Ok(Unit)
}
