package com.zsoltbertalan.flickslate.ui

import com.zsoltbertalan.flickslate.data.network.FlickSlateService
import com.zsoltbertalan.flickslate.data.network.dto.GenreResponse
import com.zsoltbertalan.flickslate.data.network.dto.MovieDetailResponse
import com.zsoltbertalan.flickslate.data.network.dto.MoviesResponseDto
import com.zsoltbertalan.flickslate.data.network.dto.NowPlayingMoviesResponse
import com.zsoltbertalan.flickslate.data.network.dto.TopRatedTvResponse
import com.zsoltbertalan.flickslate.data.network.dto.TvDetailsResponse
import com.zsoltbertalan.flickslate.data.network.dto.UpcomingMoviesResponse

val defaultFlickSlateService = object : FlickSlateService {

	override suspend fun getGenres(apiKey: String, language: String?): GenreResponse {
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

	override suspend fun getTvDetails(series_id: Int, apiKey: String): TvDetailsResponse {
		TODO("Not yet implemented")
	}

	override suspend fun getGenreMovie(with_genres: Int, page: Int?, apiKey: String): MoviesResponseDto {
		TODO("Not yet implemented")
	}
}
