package com.zsoltbertalan.flickslate.presentation.ui

import com.zsoltbertalan.flickslate.data.network.FlickSlateService
import com.zsoltbertalan.flickslate.data.network.dto.GenreReplyDto
import com.zsoltbertalan.flickslate.data.network.dto.MovieDetailsDto
import com.zsoltbertalan.flickslate.data.network.dto.MoviesReplyDto
import com.zsoltbertalan.flickslate.data.network.dto.NowPlayingMoviesReplyDto
import com.zsoltbertalan.flickslate.data.network.dto.TopRatedTvReplyDto
import com.zsoltbertalan.flickslate.data.network.dto.TvDetailsDto
import com.zsoltbertalan.flickslate.data.network.dto.UpcomingMoviesReplyDto
import retrofit2.Response

val defaultFlickSlateService = object : FlickSlateService {

	override suspend fun getGenres(
		apiKey: String,
		language: String?,
		ifNoneMatch: String
	): Response<GenreReplyDto> {
		TODO("Not yet implemented")
	}

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

	override suspend fun searchMovies(
		apiKey: String,
		language: String?,
		query: String,
		page: Int?
	): MoviesReplyDto {
		TODO("Not yet implemented")
	}

	override suspend fun getMovieDetails(movieId: Int, apiKey: String): MovieDetailsDto {
		TODO("Not yet implemented")
	}

	override suspend fun getTopRatedTv(apiKey: String, language: String?, page: Int?): Response<TopRatedTvReplyDto> {
		TODO("Not yet implemented")
	}

	override suspend fun getTvDetails(seriesId: Int, apiKey: String): TvDetailsDto {
		TODO("Not yet implemented")
	}

	override suspend fun getGenreMovie(apiKey: String, withGenres: Int, page: Int?): Response<MoviesReplyDto> {
		TODO("Not yet implemented")
	}
}
