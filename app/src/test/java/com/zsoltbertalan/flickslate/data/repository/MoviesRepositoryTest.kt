package com.zsoltbertalan.flickslate.data.repository

import androidx.paging.testing.asSnapshot
import com.zsoltbertalan.flickslate.common.testhelper.MovieDtoMother
import com.zsoltbertalan.flickslate.common.testhelper.MovieMother
import com.zsoltbertalan.flickslate.data.network.FlickSlateService
import io.kotest.matchers.equality.shouldBeEqualUsingFields
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MoviesRepositoryTest {

	private val flickSlateService: FlickSlateService = mockk()

	private lateinit var moviesAccessor: MoviesAccessor

	@Before
	fun setup() {
		coEvery {
			flickSlateService.getPopularMovies(
				any(),
				any(),
				any()
			)
		} returns MovieDtoMother.createPopularMovieList()
		coEvery {
			flickSlateService.getNowPlayingMovies(
				any(),
				any(),
				any()
			)
		} returns MovieDtoMother.createNowPlayingMovieList()
		coEvery {
			flickSlateService.getUpcomingMovies(
				any(),
				any(),
				any()
			)
		} returns MovieDtoMother.createUpcomingMovieList()
		moviesAccessor = MoviesAccessor(flickSlateService)
	}

	@Test
	fun `when getPopularMovies called then returns correct result`() = runTest {
		val popularMoviesFlow = moviesAccessor.getPopularMovies("en")
		val pagingData = MovieMother.createPopularMovieList()
		popularMoviesFlow.asSnapshot()[0] shouldBeEqualUsingFields pagingData[0]
	}

	@Test
	fun `when getUpcomingMovies called then returns correct result`() = runTest {
		val upcomingMoviesFlow = moviesAccessor.getUpcomingMovies("en")
		val pagingData = MovieMother.createUpcomingMovieList()
		upcomingMoviesFlow.asSnapshot()[0] shouldBeEqualUsingFields pagingData[0]
	}

	@Test
	fun `when getNowPlayingMovies called then returns correct result`() = runTest {
		val nowPlayingMoviesFlow = moviesAccessor.getNowPlayingMovies("en")
		val pagingData = MovieMother.createNowPlayingMovieList()
		nowPlayingMoviesFlow.asSnapshot()[0] shouldBeEqualUsingFields pagingData[0]
	}

}
