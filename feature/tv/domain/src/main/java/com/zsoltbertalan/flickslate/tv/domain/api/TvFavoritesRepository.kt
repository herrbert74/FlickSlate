package com.zsoltbertalan.flickslate.tv.domain.api

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome

interface TvFavoritesRepository {

	suspend fun setTvFavorite(accountId: Int, sessionId: String, tvId: Int, favorite: Boolean): Outcome<Unit>

}
