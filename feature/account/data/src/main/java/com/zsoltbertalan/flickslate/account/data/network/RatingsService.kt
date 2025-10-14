package com.zsoltbertalan.flickslate.account.data.network

import com.zsoltbertalan.flickslate.account.data.network.model.RatedMovieReplyDto
import com.zsoltbertalan.flickslate.account.data.network.model.RatedTvShowReplyDto
import com.zsoltbertalan.flickslate.shared.data.network.model.TvEpisodeDetailsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface RatingsService {

	@GET("account/{account_id}/rated/movies")
	suspend fun getRatedMovies(
		@Path("account_id") accountId: Int,
		@Query("session_id") sessionId: String,
	): Response<RatedMovieReplyDto>

	@GET("account/{account_id}/rated/tv")
	suspend fun getRatedTvShows(
		@Path("account_id") accountId: Int,
		@Query("session_id") sessionId: String,
	): Response<RatedTvShowReplyDto>

	@GET("account/{account_id}/rated/tv/episodes")
	suspend fun getRatedTvShowEpisodes(
		@Path("account_id") accountId: Int,
		@Query("session_id") sessionId: String,
	): Response<List<TvEpisodeDetailsDto>>

}
