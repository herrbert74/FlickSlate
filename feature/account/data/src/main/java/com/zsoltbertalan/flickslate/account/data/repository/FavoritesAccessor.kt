package com.zsoltbertalan.flickslate.account.data.repository

import com.zsoltbertalan.flickslate.account.data.api.FavoritesDataSource
import com.zsoltbertalan.flickslate.account.domain.api.FavoritesRepository
import com.zsoltbertalan.flickslate.account.domain.model.FavoriteMovie
import com.zsoltbertalan.flickslate.account.domain.model.FavoriteTvShow
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
internal class FavoritesAccessor @Inject constructor(
	private val favoritesRemoteDataSource: FavoritesDataSource.Remote,
) : FavoritesRepository {

	override suspend fun getFavoriteMovies(
		accountId: Int,
		sessionId: String,
		page: Int,
	): Outcome<PagingReply<FavoriteMovie>> {
		return favoritesRemoteDataSource.getFavoriteMovies(accountId, sessionId, page)
	}

	override suspend fun getFavoriteTvShows(
		accountId: Int,
		sessionId: String,
		page: Int,
	): Outcome<PagingReply<FavoriteTvShow>> {
		return favoritesRemoteDataSource.getFavoriteTvShows(accountId, sessionId, page)
	}
}
