package com.zsoltbertalan.flickslate.tv.data.network

import com.zsoltbertalan.flickslate.shared.data.network.model.images.ImagesReplyDto
import com.zsoltbertalan.flickslate.tv.data.network.model.TopRatedTvReplyDto
import com.zsoltbertalan.flickslate.tv.data.network.model.TvDetailsDto
import com.zsoltbertalan.flickslate.tv.data.network.model.TvEpisodeDetailsDto
import com.zsoltbertalan.flickslate.tv.data.network.model.TvSeasonDetailsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

const val URL_TV_TOP_RATED = "tv/top_rated"
const val URL_TV_DETAILS = "tv/{series_id}"
const val URL_TV_IMAGES = "tv/{series_id}/images"
const val URL_TV_SEASON_DETAILS = "tv/{series_id}/season/{season_number}"
const val URL_TV_EPISODE_DETAILS = "tv/{series_id}/season/{season_number}/episode/{episode_number}"

internal interface TvService {

	@GET(URL_TV_TOP_RATED)
	suspend fun getTopRatedTv(
		@Header("If-None-Match") ifNoneMatch: String? = null,
		@Query("language") language: String? = "en",
		@Query("page") page: Int?
	): Response<TopRatedTvReplyDto>

	@GET(URL_TV_DETAILS)
	suspend fun getTvDetails(
		@Header("If-None-Match") ifNoneMatch: String? = null,
		@Path("series_id") seriesId: Int,
		@Query("language") language: String? = "en" // Added language query parameter for consistency
	): TvDetailsDto

	@GET(URL_TV_IMAGES)
	suspend fun getTvImages(
		@Path("series_id") tvId: Int,
		@Query("language") language: String? = "en" // Added language query parameter
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
		@Query("language") language: String? = "en"
	): TvEpisodeDetailsDto

}
