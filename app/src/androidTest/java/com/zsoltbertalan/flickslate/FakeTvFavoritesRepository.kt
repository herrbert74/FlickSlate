package com.zsoltbertalan.flickslate

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.tv.domain.api.TvFavoritesRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class FakeTvFavoritesRepository @Inject constructor() : TvFavoritesRepository {

	override suspend fun setTvFavorite(
		accountId: Int,
		sessionId: String,
		tvId: Int,
		favorite: Boolean
	): Outcome<Unit> {
		AccountListTestState.setTvShowFavorite(tvId, favorite)
		return Ok(Unit)
	}
}
