package com.zsoltbertalan.flickslate.search.network

import com.zsoltbertalan.flickslate.BuildConfig
import com.zsoltbertalan.flickslate.movies.data.network.model.MoviesReplyDto
import com.zsoltbertalan.flickslate.search.data.network.model.GenreReplyDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

const val URL_GENRE = "genre/movie/list"
const val URL_MOVIES_SEARCH = "search/movie"

interface SearchService {

	@GET(URL_GENRE)
	suspend fun getGenres(
		@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
		@Query("language") language: String? = "en",
		@Header("If-None-Match") ifNoneMatch: String = ""
	): Response<GenreReplyDto>

	@GET(URL_MOVIES_SEARCH)
	suspend fun searchMovies(
		@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
		@Query("language") language: String? = "en",
		@Query("query") query: String,
		@Query("page") page: Int?
	): MoviesReplyDto

	@GET(URL_DISCOVER_MOVIE)
	suspend fun getGenreMovie(
		@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
		@Query("with_genres") withGenres: Int,
		@Query("page") page: Int?,
	): Response<MoviesReplyDto>

}
