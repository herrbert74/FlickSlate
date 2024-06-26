package com.zsoltbertalan.flickslate.data.network

import com.zsoltbertalan.flickslate.BuildConfig
import com.zsoltbertalan.flickslate.URL_DISCOVER_MOVIE
import com.zsoltbertalan.flickslate.URL_GENRE
import com.zsoltbertalan.flickslate.URL_MOVIES_NOW_PLAYING
import com.zsoltbertalan.flickslate.URL_MOVIES_POPULAR
import com.zsoltbertalan.flickslate.URL_MOVIES_SEARCH
import com.zsoltbertalan.flickslate.URL_MOVIES_UPCOMING
import com.zsoltbertalan.flickslate.URL_MOVIE_DETAILS
import com.zsoltbertalan.flickslate.URL_TV_DETAILS
import com.zsoltbertalan.flickslate.URL_TV_TOP_RATED
import com.zsoltbertalan.flickslate.data.network.dto.GenreResponse
import com.zsoltbertalan.flickslate.data.network.dto.MovieDetailResponse
import com.zsoltbertalan.flickslate.data.network.dto.MoviesResponseDto
import com.zsoltbertalan.flickslate.data.network.dto.NowPlayingMoviesResponse
import com.zsoltbertalan.flickslate.data.network.dto.TopRatedTvResponse
import com.zsoltbertalan.flickslate.data.network.dto.TvDetailsResponse
import com.zsoltbertalan.flickslate.data.network.dto.UpcomingMoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface FlickSlateService {

	@GET(URL_GENRE)
	suspend fun getGenres(
		@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
		@Query("language") language: String? = "en",
		@Header("If-None-Match") ifNoneMatch: String = ""
	): Response<GenreResponse>

	@GET(URL_MOVIES_POPULAR)
	suspend fun getPopularMovies(
		@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
		@Query("language") language: String?,
		@Query("page") page: Int?
	): MoviesResponseDto

	@GET(URL_MOVIES_NOW_PLAYING)
	suspend fun getNowPlayingMovies(
		@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
		@Query("language") language: String?,
		@Query("page") page: Int?
	): NowPlayingMoviesResponse

	@GET(URL_MOVIES_UPCOMING)
	suspend fun getUpcomingMovies(
		@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
		@Query("language") language: String?,
		@Query("page") page: Int?
	): UpcomingMoviesResponse

	@GET(URL_MOVIES_SEARCH)
	suspend fun searchMovies(
		@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
		@Query("language") language: String?,
		@Query("query") query: String,
		@Query("page") page: Int?
	): MoviesResponseDto

	@GET(URL_MOVIE_DETAILS)
	suspend fun getMovieDetails(
		@Path("movie_id") movieId: Int,
		@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
	): MovieDetailResponse

	@GET(URL_TV_TOP_RATED)
	suspend fun getTopRatedTv(
		@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
		@Query("language") language: String?,
		@Query("page") page: Int?
	): TopRatedTvResponse

	@GET(URL_TV_DETAILS)
	suspend fun getTvDetails(
		@Path("series_id") seriesId: Int,
		@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
	): TvDetailsResponse

	@GET(URL_DISCOVER_MOVIE)
	suspend fun getGenreMovie(
		@Query("with_genres") with_genres: Int,
		@Query("page") page: Int?,
		@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
	): MoviesResponseDto

}
