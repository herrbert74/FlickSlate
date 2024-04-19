package com.zsoltbertalan.flickslate.domain.api

import androidx.paging.PagingData
import com.zsoltbertalan.flickslate.domain.model.Movie
import com.zsoltbertalan.flickslate.domain.model.MovieDetail
import com.zsoltbertalan.flickslate.ext.ApiResult
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
	fun getPopularMovies(language: String): Flow<PagingData<Movie>>

	fun getNowPlayingMovies(language: String): Flow<PagingData<Movie>>

	fun getUpcomingMovies(language: String): Flow<PagingData<Movie>>

	suspend fun getMovieDetails(movieId: Int): ApiResult<MovieDetail>
}
