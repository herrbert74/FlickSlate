package com.zsoltbertalan.flickslate.search.data.network

import com.zsoltbertalan.flickslate.movies.data.network.model.MoviesReplyDto
import com.zsoltbertalan.flickslate.search.data.network.model.GenreReplyDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

const val URL_GENRE = "genre/movie/list"
const val URL_MOVIES_SEARCH = "search/movie"
const val URL_DISCOVER_MOVIE = "discover/movie"

interface SearchService {

	@GET(URL_GENRE)
	suspend fun getGenres(
		@Header("If-None-Match") ifNoneMatch: String? = null,
		@Query("language") language: String? = "en",
	): Response<GenreReplyDto>

	@GET(URL_MOVIES_SEARCH)
	suspend fun searchMovies(
		@Header("If-None-Match") ifNoneMatch: String? = null,
		@Query("language") language: String? = "en",
		@Query("query") query: String,
		@Query("page") page: Int?
	): Response<MoviesReplyDto>

	@GET(URL_DISCOVER_MOVIE)
	suspend fun getGenreMovie(
		@Header("If-None-Match") ifNoneMatch: String? = null,
		@Query("with_genres") withGenres: Int,
		@Query("page") page: Int?,
	): Response<MoviesReplyDto>

}
