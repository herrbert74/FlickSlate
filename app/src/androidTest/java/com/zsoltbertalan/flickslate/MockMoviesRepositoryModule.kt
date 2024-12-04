package com.zsoltbertalan.flickslate

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.movies.domain.api.MoviesRepository
import com.zsoltbertalan.flickslate.movies.data.repository.MoviesRepositoryModule
import com.zsoltbertalan.flickslate.shared.model.PageData
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.testhelper.MovieMother
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [MoviesRepositoryModule::class])
class MockMoviesRepositoryModule {

	@Provides
	@Singleton
	fun provideMoviesRepository(): MoviesRepository {
		val m =  mockk<MoviesRepository> {
			val popularPagingData = PagingReply(MovieMother.createPopularMovieList(), true, PageData())
			val upcomingPagingData = PagingReply(MovieMother.createUpcomingMovieList(), true, PageData())
			val pagingData = PagingReply(MovieMother.createMovieList(), true, PageData())
			coEvery { getPopularMovies(any()) } returns flowOf(Ok(popularPagingData))
			coEvery { getUpcomingMovies(any()) } returns flowOf(Ok(upcomingPagingData))
			coEvery { getNowPlayingMovies(any()) } returns flowOf(Ok(pagingData))

		}
		coEvery { m.getMovieDetails(any()) } returns Ok(MovieMother.createMovieDetail())
		return m
	}

}
