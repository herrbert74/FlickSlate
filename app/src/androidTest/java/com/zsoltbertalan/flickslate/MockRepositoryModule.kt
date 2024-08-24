package com.zsoltbertalan.flickslate

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.common.testhelper.GenreMother
import com.zsoltbertalan.flickslate.common.testhelper.MovieMother
import com.zsoltbertalan.flickslate.common.testhelper.TvMother
import com.zsoltbertalan.flickslate.data.network.NetworkModule
import com.zsoltbertalan.flickslate.domain.api.GenreRepository
import com.zsoltbertalan.flickslate.domain.api.MoviesRepository
import com.zsoltbertalan.flickslate.domain.api.SearchRepository
import com.zsoltbertalan.flickslate.domain.api.TvRepository
import com.zsoltbertalan.flickslate.domain.model.PagingReply
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [NetworkModule.RepositoryModule::class])
class MockRepositoryModule {

	@Provides
	@Singleton
	fun provideGenreRepository(): GenreRepository {
		return mockk {
			coEvery { getGenresList() } returns flowOf(Ok(GenreMother.createGenreList()))
		}
	}

	@Provides
	@Singleton
	fun provideMoviesRepository(): MoviesRepository {
		val m = mockk<MoviesRepository> {
			val popularPagingData = PagingReply(MovieMother.createPopularMovieList(), true)
			val upcomingPagingData = PagingReply(MovieMother.createUpcomingMovieList(), true)
			val pagingData = PagingReply(MovieMother.createMovieList(), true)

			coEvery { getPopularMovies(any()) } returns flowOf(Ok(popularPagingData))
			coEvery { getUpcomingMovies(any()) } returns flowOf(Ok(upcomingPagingData))
			coEvery { getNowPlayingMovies(any()) } returns flowOf(Ok(pagingData))
		}

		m.also {
			coEvery { it.getMovieDetails(any()) } returns Ok(MovieMother.createMovieDetail())
		}
		return m
	}

	@Provides
	@Singleton
	fun provideSearchRepository(): SearchRepository {
		return mockk {

		}
	}

	@Provides
	@Singleton
	fun provideTvRepository(): TvRepository {
		return mockk {
			val pagingData = PagingReply(TvMother.createTvList(), true)
			coEvery { getTopRatedTv(any()) } returns flowOf(Ok(pagingData))
		}
	}

}
