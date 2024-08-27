package com.zsoltbertalan.flickslate.movies.data.network

import com.zsoltbertalan.flickslate.BuildConfig
import com.zsoltbertalan.flickslate.movies.data.network.model.MovieDetailsDto
import com.zsoltbertalan.flickslate.movies.data.network.model.MoviesReplyDto
import com.zsoltbertalan.flickslate.movies.data.network.model.NowPlayingMoviesReplyDto
import com.zsoltbertalan.flickslate.movies.data.network.model.UpcomingMoviesReplyDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val URL_MOVIES_POPULAR = "movie/popular"
const val URL_MOVIES_NOW_PLAYING = "movie/now_playing"
const val URL_MOVIES_UPCOMING = "movie/upcoming"
const val URL_MOVIE_DETAILS = "movie/{movie_id}"

interface MoviesService {

	@GET(URL_MOVIES_POPULAR)
	suspend fun getPopularMovies(
		@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
		@Query("language") language: String? = "en",
		@Query("page") page: Int?
	): Response<MoviesReplyDto>

	@GET(URL_MOVIES_NOW_PLAYING)
	suspend fun getNowPlayingMovies(
		@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
		@Query("language") language: String? = "en",
		@Query("page") page: Int?
	): Response<NowPlayingMoviesReplyDto>

	@GET(URL_MOVIES_UPCOMING)
	suspend fun getUpcomingMovies(
		@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
		@Query("language") language: String? = "en",
		@Query("page") page: Int?
	): Response<UpcomingMoviesReplyDto>

	@GET(URL_MOVIE_DETAILS)
	suspend fun getMovieDetails(
		@Path("movie_id") movieId: Int,
		@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
	): MovieDetailsDto

}
