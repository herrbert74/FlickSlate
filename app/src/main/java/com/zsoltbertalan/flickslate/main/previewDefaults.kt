package com.zsoltbertalan.flickslate.main

import com.zsoltbertalan.flickslate.movies.data.network.model.MovieDetailsDto
import com.zsoltbertalan.flickslate.movies.data.network.model.MoviesReplyDto
import com.zsoltbertalan.flickslate.movies.data.network.model.NowPlayingMoviesReplyDto
import com.zsoltbertalan.flickslate.movies.data.network.model.UpcomingMoviesReplyDto
import com.zsoltbertalan.flickslate.movies.data.network.MoviesService
import com.zsoltbertalan.flickslate.search.data.network.model.GenreReplyDto
import com.zsoltbertalan.flickslate.search.data.network.SearchService
import com.zsoltbertalan.flickslate.tv.data.network.TvService
import com.zsoltbertalan.flickslate.tv.data.network.model.TopRatedTvReplyDto
import com.zsoltbertalan.flickslate.tv.data.network.model.TvDetailsDto
import retrofit2.Response

val defaultMoviesService = object : MoviesService {

	override suspend fun getPopularMovies(
		apiKey: String,
		language: String?,
		page: Int?
	): Response<MoviesReplyDto> {
		TODO("Not yet implemented")
	}

	override suspend fun getNowPlayingMovies(
		apiKey: String,
		language: String?,
		page: Int?
	): Response<NowPlayingMoviesReplyDto> {
		TODO("Not yet implemented")
	}

	override suspend fun getUpcomingMovies(
		apiKey: String,
		language: String?,
		page: Int?
	): Response<UpcomingMoviesReplyDto> {
		TODO("Not yet implemented")
	}

	override suspend fun getMovieDetails(movieId: Int, apiKey: String): MovieDetailsDto {
		TODO("Not yet implemented")
	}

}

val defaultSearchService = object : SearchService {

	override suspend fun getGenres(
		apiKey: String,
		language: String?,
		ifNoneMatch: String
	): Response<GenreReplyDto> {
		TODO("Not yet implemented")
	}

	override suspend fun searchMovies(
		apiKey: String,
		language: String?,
		query: String,
		page: Int?
	): MoviesReplyDto {
		TODO("Not yet implemented")
	}

	override suspend fun getGenreMovie(apiKey: String, withGenres: Int, page: Int?): Response<MoviesReplyDto> {
		TODO("Not yet implemented")
	}
}

val defaultTvService = object : TvService {

	override suspend fun getTopRatedTv(apiKey: String, language: String?, page: Int?): Response<TopRatedTvReplyDto> {
		TODO("Not yet implemented")
	}

	override suspend fun getTvDetails(seriesId: Int, apiKey: String): TvDetailsDto {
		TODO("Not yet implemented")
	}

}
