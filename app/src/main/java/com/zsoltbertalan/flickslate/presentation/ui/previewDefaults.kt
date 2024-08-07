package com.zsoltbertalan.flickslate.presentation.ui

import com.zsoltbertalan.flickslate.data.network.FlickSlateService
import com.zsoltbertalan.flickslate.data.network.dto.GenreReply
import com.zsoltbertalan.flickslate.data.network.dto.MovieDetailResponse
import com.zsoltbertalan.flickslate.data.network.dto.MoviesResponseDto
import com.zsoltbertalan.flickslate.data.network.dto.NowPlayingMoviesResponse
import com.zsoltbertalan.flickslate.data.network.dto.TopRatedTvResponse
import com.zsoltbertalan.flickslate.data.network.dto.TvDetailsResponse
import com.zsoltbertalan.flickslate.data.network.dto.UpcomingMoviesResponse
import retrofit2.Response

val defaultFlickSlateService = object : FlickSlateService {

	override suspend fun getGenres(
		apiKey: String,
		language: String?,
		ifNoneMatch: String
	): Response<GenreReply> {
		TODO("Not yet implemented")
	}

	override suspend fun getPopularMovies(
		apiKey: String,
		language: String?,
		page: Int?
	): MoviesResponseDto {
		TODO("Not yet implemented")
	}

	override suspend fun getNowPlayingMovies(
		apiKey: String,
		language: String?,
		page: Int?
	): NowPlayingMoviesResponse {
		TODO("Not yet implemented")
	}

	override suspend fun getUpcomingMovies(
		apiKey: String,
		language: String?,
		page: Int?
	): UpcomingMoviesResponse {
		TODO("Not yet implemented")
	}

	override suspend fun searchMovies(
		apiKey: String,
		language: String?,
		query: String,
		page: Int?
	): MoviesResponseDto {
		TODO("Not yet implemented")
	}

	override suspend fun getMovieDetails(movieId: Int, apiKey: String): MovieDetailResponse {
		TODO("Not yet implemented")
	}

	override suspend fun getTopRatedTv(apiKey: String, language: String?, page: Int?): TopRatedTvResponse {
		TODO("Not yet implemented")
	}

	override suspend fun getTvDetails(seriesId: Int, apiKey: String): TvDetailsResponse {
		TODO("Not yet implemented")
	}

	override suspend fun getGenreMovie(withGenres: Int, page: Int?, apiKey: String): MoviesResponseDto {
		TODO("Not yet implemented")
	}
}
