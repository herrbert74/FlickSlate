package com.zsoltbertalan.flickslate

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.movies.domain.api.MoviesRepository
import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetail
import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetailMother
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.MovieMother
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.domain.model.images.ImagesReply
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeMoviesRepository @Inject constructor() : MoviesRepository {

	var movieDetail: MovieDetail = MovieDetailMother.createMovieDetail()

	override fun getPopularMovies(page: Int): Flow<Outcome<PagingReply<Movie>>> = flowOf(
		Ok(
			PagingReply(MovieMother.createPopularMovieList(), true, PageData())
		)
	)

	override fun getNowPlayingMovies(page: Int): Flow<Outcome<PagingReply<Movie>>> = flowOf(
		Ok(
			PagingReply(MovieMother.createMovieList(), true, PageData())
		)
	)

	override fun getUpcomingMovies(page: Int): Flow<Outcome<PagingReply<Movie>>> = flowOf(
		Ok(
			PagingReply(MovieMother.createUpcomingMovieList(), true, PageData())
		)
	)

	override suspend fun getMovieDetails(movieId: Int, sessionId: String?): Outcome<MovieDetail> =
		Ok(movieDetail)

	override suspend fun getMovieImages(movieId: Int): Outcome<ImagesReply> = Ok(MovieMother.createMovieImages())

}
