package com.zsoltbertalan.flickslate

import androidx.paging.PagingData
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.data.network.NetworkModule
import com.zsoltbertalan.flickslate.domain.api.GenreRepository
import com.zsoltbertalan.flickslate.domain.api.MoviesRepository
import com.zsoltbertalan.flickslate.domain.api.SearchRepository
import com.zsoltbertalan.flickslate.domain.api.TvRepository
import com.zsoltbertalan.flickslate.common.testhelper.GenreMother
import com.zsoltbertalan.flickslate.common.testhelper.MovieMother
import com.zsoltbertalan.flickslate.common.testhelper.TvMother
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
			val popularPagingData = PagingData.from(
				MovieMother.createPopularMovieList()
			)
			val upcomingPagingData = PagingData.from(
				MovieMother.createUpcomingMovieList()
			)
			val pagingData = PagingData.from(
				MovieMother.createMovieList()
			)
			coEvery { getPopularMovies(any()) } returns flowOf(popularPagingData)
			coEvery { getUpcomingMovies(any()) } returns flowOf(upcomingPagingData)
			coEvery { getNowPlayingMovies(any()) } returns flowOf(pagingData)

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
			val pagingData = PagingData.from(
				TvMother.createTvList()
			)
			coEvery { getTopRatedTv(any()) } returns flowOf(pagingData)
		}
	}

}
