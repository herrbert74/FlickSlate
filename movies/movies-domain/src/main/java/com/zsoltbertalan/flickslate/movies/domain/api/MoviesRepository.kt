package com.zsoltbertalan.flickslate.movies.domain.api

import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetail
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
	fun getPopularMovies(page: Int): Flow<Outcome<PagingReply<Movie>>>

	fun getNowPlayingMovies(page: Int): Flow<Outcome<PagingReply<Movie>>>

	fun getUpcomingMovies(page: Int): Flow<Outcome<PagingReply<Movie>>>

	suspend fun getMovieDetails(movieId: Int): Outcome<MovieDetail>

}
