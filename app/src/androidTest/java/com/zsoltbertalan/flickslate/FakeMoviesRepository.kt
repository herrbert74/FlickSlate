package com.zsoltbertalan.flickslate

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.movies.data.repository.MoviesAccessor
import com.zsoltbertalan.flickslate.movies.domain.api.MoviesRepository
import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetail
import com.zsoltbertalan.flickslate.shared.model.Movie
import com.zsoltbertalan.flickslate.shared.model.PageData
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import com.zsoltbertalan.flickslate.testhelper.MovieMother
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import se.ansman.dagger.auto.android.testing.Replaces
import javax.inject.Inject

@Replaces(MoviesAccessor::class)
@ActivityRetainedScoped
class FakeMoviesRepository @Inject constructor(): MoviesRepository {

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

	override suspend fun getMovieDetails(movieId: Int): Outcome<MovieDetail> = Ok(MovieMother.createMovieDetail())

}
