package com.zsoltbertalan.flickslate.account.data.network

import com.zsoltbertalan.flickslate.account.data.network.model.FavoriteMovieReplyDto
import com.zsoltbertalan.flickslate.account.data.network.model.FavoriteTvShowReplyDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FavoritesService {

	@GET("account/{account_id}/favorite/movies")
	suspend fun getFavoriteMovies(
		@Path("account_id") accountId: Int,
		@Query("session_id") sessionId: String,
		@Query("page") page: Int,
	): Response<FavoriteMovieReplyDto>

	@GET("account/{account_id}/favorite/tv")
	suspend fun getFavoriteTvShows(
		@Path("account_id") accountId: Int,
		@Query("session_id") sessionId: String,
		@Query("page") page: Int,
	): Response<FavoriteTvShowReplyDto>
}
