package com.zsoltbertalan.flickslate.tv.data.network

import com.zsoltbertalan.flickslate.shared.data.network.model.TvEpisodeDetailsDto
import com.zsoltbertalan.flickslate.shared.data.network.model.images.ImagesReplyDto
import com.zsoltbertalan.flickslate.tv.data.network.model.TopRatedTvReplyDto
import com.zsoltbertalan.flickslate.tv.data.network.model.TvDetailsDto
import com.zsoltbertalan.flickslate.tv.data.network.model.TvSeasonDetailsDto
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

const val URL_TV_TOP_RATED = "tv/top_rated"
const val URL_TV_DETAILS = "tv/{series_id}"
const val URL_TV_IMAGES = "tv/{series_id}/images"
const val URL_TV_SEASON_DETAILS = "tv/{series_id}/season/{season_number}"
const val URL_TV_EPISODE_DETAILS = "tv/{series_id}/season/{season_number}/episode/{episode_number}"
const val URL_TV_RATE = "tv/{tv_show_id}/rating"
const val URL_TV_EPISODE_RATE = "tv/{tv_show_id}/season/{season_number}/episode/{episode_number}/rating"

internal interface TvService {

	@GET(URL_TV_TOP_RATED)
	suspend fun getTopRatedTv(
		@Header("If-None-Match") ifNoneMatch: String? = null,
		@Query("language") language: String? = "en",
		@Query("page") page: Int?
	): Response<TopRatedTvReplyDto>

	@GET(URL_TV_DETAILS)
	suspend fun getTvDetails(
		@Path("series_id") seriesId: Int,
		@Header("If-None-Match") ifNoneMatch: String? = null,
		@Query("language") language: String? = "en",
		@Query("session_id") sessionId: String? = null,
		@Query("append_to_response") appendToResponse: String? = null,
	): TvDetailsDto

	@GET(URL_TV_IMAGES)
	suspend fun getTvImages(
		@Path("series_id") tvId: Int,
		@Query("language") language: String? = "en"
	): ImagesReplyDto

	@GET(URL_TV_SEASON_DETAILS)
	suspend fun getTvSeasonDetails(
		@Header("If-None-Match") ifNoneMatch: String? = null,
		@Path("series_id") seriesId: Int,
		@Path("season_number") seasonNumber: Int,
		@Query("language") language: String? = "en"
	): Response<TvSeasonDetailsDto>

	@GET(URL_TV_EPISODE_DETAILS)
	suspend fun getTvEpisodeDetails(
		@Header("If-None-Match") ifNoneMatch: String? = null,
		@Path("series_id") seriesId: Int,
		@Path("season_number") seasonNumber: Int,
		@Path("episode_number") episodeNumber: Int,
		@Query("language") language: String? = "en",
		@Query("session_id") sessionId: String? = null,
		@Query("append_to_response") appendToResponse: String? = null,
	): TvEpisodeDetailsDto

	@POST(URL_TV_RATE)
	suspend fun rateTvShow(
		@Path("tv_show_id") tvShowId: Int,
		@Query("session_id") sessionId: String,
		@Body rating: RequestBody
	)

	@DELETE(URL_TV_RATE)
	suspend fun deleteTvShowRating(
		@Path("tv_show_id") tvShowId: Int,
		@Query("session_id") sessionId: String
	)

	@POST(URL_TV_EPISODE_RATE)
	suspend fun rateTvShowEpisode(
		@Path("tv_show_id") tvShowId: Int,
		@Path("season_number") seasonNumber: Int,
		@Path("episode_number") episodeNumber: Int,
		@Query("session_id") sessionId: String,
		@Body rating: RequestBody
	)

	@DELETE(URL_TV_EPISODE_RATE)
	suspend fun deleteTvShowEpisodeRating(
		@Path("tv_show_id") tvShowId: Int,
		@Path("season_number") seasonNumber: Int,
		@Path("episode_number") episodeNumber: Int,
		@Query("session_id") sessionId: String
	)

}
