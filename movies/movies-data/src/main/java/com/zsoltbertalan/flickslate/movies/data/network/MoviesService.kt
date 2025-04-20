package com.zsoltbertalan.flickslate.movies.data.network

import com.zsoltbertalan.flickslate.movies.data.network.model.MovieDetailsDto
import com.zsoltbertalan.flickslate.shared.data.network.model.MoviesReplyDto
import com.zsoltbertalan.flickslate.movies.data.network.model.NowPlayingMoviesReplyDto
import com.zsoltbertalan.flickslate.movies.data.network.model.UpcomingMoviesReplyDto
import com.zsoltbertalan.flickslate.shared.data.network.model.images.ImagesReplyDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

const val URL_MOVIES_POPULAR = "movie/popular"
const val URL_MOVIES_NOW_PLAYING = "movie/now_playing"
const val URL_MOVIES_UPCOMING = "movie/upcoming"
const val URL_MOVIE_DETAILS = "movie/{movie_id}"
const val URL_MOVIE_IMAGES = "movie/{movie_id}/images"

interface MoviesService {

	@GET(URL_MOVIES_POPULAR)
	suspend fun getPopularMovies(
		@Header("If-None-Match") ifNoneMatch: String? = null,
		@Query("language") language: String? = "en",
		@Query("page") page: Int?
	): Response<MoviesReplyDto>

	@GET(URL_MOVIES_NOW_PLAYING)
	suspend fun getNowPlayingMovies(
		@Header("If-None-Match") ifNoneMatch: String? = null,
		@Query("language") language: String? = "en",
		@Query("page") page: Int?
	): Response<NowPlayingMoviesReplyDto>

	@GET(URL_MOVIES_UPCOMING)
	suspend fun getUpcomingMovies(
		@Header("If-None-Match") ifNoneMatch: String? = null,
		@Query("language") language: String? = "en",
		@Query("page") page: Int?
	): Response<UpcomingMoviesReplyDto>

	@GET(URL_MOVIE_DETAILS)
	suspend fun getMovieDetails(
		@Header("If-None-Match") ifNoneMatch: String? = null,
		@Path("movie_id") movieId: Int,
	): MovieDetailsDto

	@GET(URL_MOVIE_IMAGES)
	suspend fun getMovieImages(
		@Path("movie_id") movieId: Int
	): ImagesReplyDto

}
