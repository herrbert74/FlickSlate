package com.zsoltbertalan.flickslate.tv.data.api

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome

interface TvFavoritesDataSource {

	interface Remote {

		suspend fun setTvFavorite(accountId: Int, sessionId: String, tvId: Int, favorite: Boolean): Outcome<Unit>

	}

}
