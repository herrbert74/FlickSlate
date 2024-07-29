package com.zsoltbertalan.flickslate.data.repository

import com.github.michaelbull.result.map
import com.zsoltbertalan.flickslate.data.network.FlickSlateService
import com.zsoltbertalan.flickslate.data.network.dto.toMovieDetail
import com.zsoltbertalan.flickslate.data.network.dto.toMoviesResponse
import com.zsoltbertalan.flickslate.domain.api.MoviesRepository
import com.zsoltbertalan.flickslate.domain.model.MovieDetail
import com.zsoltbertalan.flickslate.ext.Outcome
import com.zsoltbertalan.flickslate.ext.runCatchingApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesAccessor @Inject constructor(private val flickSlateService: FlickSlateService) : MoviesRepository {

	override fun getPopularMovies(language: String) = createPager { page ->
		flickSlateService.runCatchingApi {
			getPopularMovies(language = language, page = page)
		}.map { Pair(it.toMoviesResponse().movies, it.total_pages ?: 0) }
	}.flow

	override fun getNowPlayingMovies(language: String) = createPager { page ->
		flickSlateService.runCatchingApi {
			getNowPlayingMovies(language = language, page = page)
		}.map { Pair(it.toMoviesResponse().movies, it.total_pages ?: 0) }
	}.flow

	override fun getUpcomingMovies(language: String) = createPager { page ->
		flickSlateService.runCatchingApi {
			getUpcomingMovies(language = language, page = page)
		}.map { Pair(it.toMoviesResponse().movies, it.total_pages ?: 0) }
	}.flow

	override suspend fun getMovieDetails(movieId: Int): Outcome<MovieDetail> {
		return flickSlateService.runCatchingApi {
			getMovieDetails(movieId = movieId).toMovieDetail()
		}
	}

}
