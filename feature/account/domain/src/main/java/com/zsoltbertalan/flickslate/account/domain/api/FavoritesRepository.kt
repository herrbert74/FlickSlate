package com.zsoltbertalan.flickslate.account.domain.api

import com.zsoltbertalan.flickslate.account.domain.model.FavoriteMovie
import com.zsoltbertalan.flickslate.account.domain.model.FavoriteTvShow
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply

interface FavoritesRepository {

	suspend fun getFavoriteMovies(accountId: Int, sessionId: String, page: Int): Outcome<PagingReply<FavoriteMovie>>

	suspend fun getFavoriteTvShows(accountId: Int, sessionId: String, page: Int): Outcome<PagingReply<FavoriteTvShow>>
}
