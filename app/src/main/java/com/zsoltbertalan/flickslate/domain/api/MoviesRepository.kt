package com.zsoltbertalan.flickslate.domain.api

import com.zsoltbertalan.flickslate.common.util.Outcome
import com.zsoltbertalan.flickslate.domain.model.Movie
import com.zsoltbertalan.flickslate.domain.model.MovieDetail
import com.zsoltbertalan.flickslate.domain.model.PagingReply
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
	fun getPopularMovies(page: Int): Flow<Outcome<PagingReply<Movie>>>

	fun getNowPlayingMovies(page: Int):Flow<Outcome<PagingReply<Movie>>>

	fun getUpcomingMovies(page: Int): Flow<Outcome<PagingReply<Movie>>>

	suspend fun getMovieDetails(movieId: Int): Outcome<MovieDetail>

}
