package com.zsoltbertalan.flickslate.movies.data.network

import com.zsoltbertalan.flickslate.movies.data.network.model.MovieDetailsDto
import com.zsoltbertalan.flickslate.movies.data.network.model.NowPlayingMoviesReplyDto
import com.zsoltbertalan.flickslate.movies.data.network.model.UpcomingMoviesReplyDto
import com.zsoltbertalan.flickslate.shared.data.network.model.MoviesReplyDto
import com.zsoltbertalan.flickslate.shared.data.network.model.images.ImagesReplyDto
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

private const val URL_MOVIES_POPULAR = "movie/popular"
private const val URL_MOVIES_NOW_PLAYING = "movie/now_playing"
private const val URL_MOVIES_UPCOMING = "movie/upcoming"
private const val URL_MOVIE_DETAILS = "movie/{movie_id}"
private const val URL_MOVIE_IMAGES = "movie/{movie_id}/images"
private const val URL_MOVIE_RATE = "movie/{movie_id}/rating"

internal interface MoviesService {

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

	@POST(URL_MOVIE_RATE)
	suspend fun rateMovie(
		@Path("movie_id") movieId: Int,
		@Query("session_id") sessionId: String,
		@Body rating: RequestBody
	)

	@DELETE(URL_MOVIE_RATE)
	suspend fun deleteMovieRating(
		@Path("movie_id") movieId: Int,
		@Query("session_id") sessionId: String
	)

}
