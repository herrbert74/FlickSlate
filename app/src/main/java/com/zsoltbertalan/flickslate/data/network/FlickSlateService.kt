package com.zsoltbertalan.flickslate.data.network

import com.zsoltbertalan.flickslate.BuildConfig
import com.zsoltbertalan.flickslate.data.network.dto.GenreReplyDto
import com.zsoltbertalan.flickslate.data.network.dto.MovieDetailsDto
import com.zsoltbertalan.flickslate.data.network.dto.MoviesReplyDto
import com.zsoltbertalan.flickslate.data.network.dto.NowPlayingMoviesReplyDto
import com.zsoltbertalan.flickslate.data.network.dto.TopRatedTvReplyDto
import com.zsoltbertalan.flickslate.data.network.dto.TvDetailsDto
import com.zsoltbertalan.flickslate.data.network.dto.UpcomingMoviesReplyDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

const val URL_GENRE = "genre/movie/list"
const val URL_MOVIES_POPULAR = "movie/popular"
const val URL_MOVIES_NOW_PLAYING = "movie/now_playing"
const val URL_MOVIES_UPCOMING = "movie/upcoming"
const val URL_MOVIES_SEARCH = "search/movie"
const val URL_MOVIE_DETAILS = "movie/{movie_id}"
const val URL_TV_TOP_RATED = "tv/top_rated"
const val URL_TV_DETAILS = "tv/{series_id}"
const val URL_DISCOVER_MOVIE = "discover/movie"

interface FlickSlateService {

	@GET(URL_GENRE)
	suspend fun getGenres(
		@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
		@Query("language") language: String? = "en",
		@Header("If-None-Match") ifNoneMatch: String = ""
	): Response<GenreReplyDto>

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

	@GET(URL_MOVIES_SEARCH)
	suspend fun searchMovies(
		@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
		@Query("language") language: String? = "en",
		@Query("query") query: String,
		@Query("page") page: Int?
	): MoviesReplyDto

	@GET(URL_MOVIE_DETAILS)
	suspend fun getMovieDetails(
		@Path("movie_id") movieId: Int,
		@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
	): MovieDetailsDto

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

	@GET(URL_DISCOVER_MOVIE)
	suspend fun getGenreMovie(
		@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
		@Query("with_genres") withGenres: Int,
		@Query("page") page: Int?,
	): Response<MoviesReplyDto>

}
