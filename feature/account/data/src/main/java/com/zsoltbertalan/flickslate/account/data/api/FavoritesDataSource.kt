package com.zsoltbertalan.flickslate.account.data.api

import com.zsoltbertalan.flickslate.account.domain.model.FavoriteMovie
import com.zsoltbertalan.flickslate.account.domain.model.FavoriteTvShow
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome

internal interface FavoritesDataSource {

	interface Remote {

		suspend fun getFavoriteMovies(accountId: Int, sessionId: String, page: Int): Outcome<PagingReply<FavoriteMovie>>

		suspend fun getFavoriteTvShows(accountId: Int, sessionId: String, page: Int): Outcome<PagingReply<FavoriteTvShow>>
	}
}
