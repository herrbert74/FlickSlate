package com.zsoltbertalan.flickslate.tv.data.network

import com.zsoltbertalan.flickslate.BuildConfig
import com.zsoltbertalan.flickslate.tv.data.network.model.TopRatedTvReplyDto
import com.zsoltbertalan.flickslate.tv.data.network.model.TvDetailsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val URL_TV_TOP_RATED = "tv/top_rated"
const val URL_TV_DETAILS = "tv/{series_id}"

interface TvService {

	@GET(URL_TV_TOP_RATED)
	suspend fun getTopRatedTv(
		@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
		@Query("language") language: String? = "en",
		@Query("page") page: Int?
	): Response<TopRatedTvReplyDto>

	@GET(URL_TV_DETAILS)
	suspend fun getTvDetails(
		@Path("series_id") seriesId: Int,
		@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
	): TvDetailsDto

}
