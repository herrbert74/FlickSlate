package com.zsoltbertalan.flickslate.data.repository

import com.github.michaelbull.result.map
import com.zsoltbertalan.flickslate.data.network.FlickSlateService
import com.zsoltbertalan.flickslate.data.network.dto.toMovieDetail
import com.zsoltbertalan.flickslate.data.network.dto.toMoviesResponse
import com.zsoltbertalan.flickslate.domain.api.MoviesRepository
import com.zsoltbertalan.flickslate.domain.model.MovieDetail
import com.zsoltbertalan.flickslate.ext.ApiResult
import com.zsoltbertalan.flickslate.ext.apiRunCatching
import javax.inject.Singleton

@Singleton
class MoviesAccessor(private val flickSlateService: FlickSlateService) : MoviesRepository {

	override fun getPopularMovies(language: String) = createPager { page ->
		apiRunCatching {
			flickSlateService.getPopularMovies(language = language, page = page)
		}.map { Pair(it.toMoviesResponse().movies, it.total_pages ?: 0) }
	}.flow

	override fun getNowPlayingMovies(language: String) = createPager { page ->
		apiRunCatching {
			flickSlateService.getNowPlayingMovies(language = language, page = page)
		}.map { Pair(it.toMoviesResponse().movies, it.total_pages ?: 0) }
	}.flow

	override fun getUpcomingMovies(language: String) = createPager { page ->
		apiRunCatching {
			flickSlateService.getUpcomingMovies(language = language, page = page)
		}.map { Pair(it.toMoviesResponse().movies, it.total_pages ?: 0) }
	}.flow

	override suspend fun getMovieDetails(movieId: Int): ApiResult<MovieDetail> {
		return apiRunCatching {
			flickSlateService.getMovieDetails(movieId = movieId).toMovieDetail()
		}
	}

}
