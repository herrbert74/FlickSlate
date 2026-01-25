package com.zsoltbertalan.flickslate.account.data.network

import com.zsoltbertalan.flickslate.account.data.api.FavoritesDataSource
import com.zsoltbertalan.flickslate.account.data.network.model.FavoriteMovieReplyDto
import com.zsoltbertalan.flickslate.account.data.network.model.FavoriteTvShowReplyDto
import com.zsoltbertalan.flickslate.account.data.network.model.toFavoriteMoviesReply
import com.zsoltbertalan.flickslate.account.data.network.model.toFavoriteTvShowsReply
import com.zsoltbertalan.flickslate.account.domain.model.FavoriteMovie
import com.zsoltbertalan.flickslate.account.domain.model.FavoriteTvShow
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.shared.data.util.safeCallWithMetadata
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Response
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ActivityRetainedScoped
internal class FavoritesRemoteDataSource @Inject constructor(
	private val favoritesService: FavoritesService,
) : FavoritesDataSource.Remote {

	override suspend fun getFavoriteMovies(
		accountId: Int,
		sessionId: String,
		page: Int,
	): Outcome<PagingReply<FavoriteMovie>> {
		return safeCallWithMetadata(
			{ favoritesService.getFavoriteMovies(accountId, sessionId, page) },
			Response<FavoriteMovieReplyDto>::toFavoriteMoviesReply,
		)
	}

	override suspend fun getFavoriteTvShows(
		accountId: Int,
		sessionId: String,
		page: Int,
	): Outcome<PagingReply<FavoriteTvShow>> {
		return safeCallWithMetadata(
			{ favoritesService.getFavoriteTvShows(accountId, sessionId, page) },
			Response<FavoriteTvShowReplyDto>::toFavoriteTvShowsReply,
		)
	}
}
