package com.zsoltbertalan.flickslate.account.data.network

import com.zsoltbertalan.flickslate.account.data.network.model.RatedMovieReplyDto
import com.zsoltbertalan.flickslate.account.data.network.model.RatedTvShowReplyDto
import com.zsoltbertalan.flickslate.shared.data.network.model.TvEpisodeDetailsDto
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

internal interface RatingsService {

	@GET("account/{account_id}/rated/movies")
	suspend fun getRatedMovies(
		@Path("account_id") accountId: Int,
		@Query("session_id") sessionId: String,
	): RatedMovieReplyDto

	@GET("account/{account_id}/rated/tv")
	suspend fun getRatedTvShows(
		@Path("account_id") accountId: Int,
		@Query("session_id") sessionId: String,
	): RatedTvShowReplyDto

	@GET("account/{account_id}/rated/tv/episodes")
	suspend fun getRatedTvShowEpisodes(
		@Path("account_id") accountId: Int,
		@Query("session_id") sessionId: String,
	): List<TvEpisodeDetailsDto>

	@POST("movie/{movie_id}/rating")
	suspend fun rateMovie(
		@Path("movie_id") movieId: Int,
		@Query("session_id") sessionId: String,
		@Body rating: RequestBody
	)

	@DELETE("movie/{movie_id}/rating")
	suspend fun deleteMovieRating(
		@Path("movie_id") movieId: Int,
		@Query("session_id") sessionId: String
	)

	@POST("tv/{tv_show_id}/rating")
	suspend fun rateTvShow(
		@Path("tv_show_id") tvShowId: Int,
		@Query("session_id") sessionId: String,
		@Body rating: RequestBody
	)

	@DELETE("tv/{tv_show_id}/rating")
	suspend fun deleteTvShowRating(
		@Path("tv_show_id") tvShowId: Int,
		@Query("session_id") sessionId: String
	)

	@POST("tv/{tv_show_id}/season/{season_number}/episode/{episode_number}/rating")
	suspend fun rateTvShowEpisode(
		@Path("tv_show_id") tvShowId: Int,
		@Path("season_number") seasonNumber: Int,
		@Path("episode_number") episodeNumber: Int,
		@Query("session_id") sessionId: String,
		@Body rating: RequestBody
	)

	@DELETE("tv/{tv_show_id}/season/{season_number}/episode/{episode_number}/rating")
	suspend fun deleteTvShowEpisodeRating(
		@Path("tv_show_id") tvShowId: Int,
		@Path("season_number") seasonNumber: Int,
		@Path("episode_number") episodeNumber: Int,
		@Query("session_id") sessionId: String
	)

}
