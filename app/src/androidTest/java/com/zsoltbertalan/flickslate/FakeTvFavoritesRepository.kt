package com.zsoltbertalan.flickslate

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.tv.domain.api.TvFavoritesRepository

class FakeTvFavoritesRepository : TvFavoritesRepository {

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
