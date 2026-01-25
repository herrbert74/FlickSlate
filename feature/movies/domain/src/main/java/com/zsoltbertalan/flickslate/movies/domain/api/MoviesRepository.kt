package com.zsoltbertalan.flickslate.movies.domain.api

import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetail
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.domain.model.images.ImagesReply
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

	fun getPopularMovies(page: Int): Flow<Outcome<PagingReply<Movie>>>
	fun getNowPlayingMovies(page: Int): Flow<Outcome<PagingReply<Movie>>>
	fun getUpcomingMovies(page: Int): Flow<Outcome<PagingReply<Movie>>>
	suspend fun getMovieDetails(movieId: Int, sessionId: String? = null): Outcome<MovieDetail>
	suspend fun getMovieImages(movieId: Int): Outcome<ImagesReply>

}
