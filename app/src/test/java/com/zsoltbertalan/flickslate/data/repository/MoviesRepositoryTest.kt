package com.zsoltbertalan.flickslate.data.repository

import com.zsoltbertalan.flickslate.common.testhelper.MovieDtoMother
import com.zsoltbertalan.flickslate.common.testhelper.MovieMother
import com.zsoltbertalan.flickslate.data.db.NowPlayingMoviesDataSource
import com.zsoltbertalan.flickslate.data.db.PopularMoviesDataSource
import com.zsoltbertalan.flickslate.data.db.UpcomingMoviesDataSource
import com.zsoltbertalan.flickslate.data.network.FlickSlateService
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.equality.shouldBeEqualUsingFields
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class MoviesRepositoryTest {

	private val flickSlateService: FlickSlateService = mockk()

	private val popularMoviesDataSource: PopularMoviesDataSource = mockk()
	private val upcomingMoviesDataSource: UpcomingMoviesDataSource = mockk()
	private val nowPlayingMoviesDataSource: NowPlayingMoviesDataSource = mockk()

	private lateinit var moviesAccessor: MoviesAccessor

	@Before
	fun setup() {
		coEvery {
			flickSlateService.getPopularMovies(any(), any(), any())
		} returns Response.success(MovieDtoMother.createPopularMovieList())
		coEvery {
			flickSlateService.getNowPlayingMovies(any(), any(), any())
		} returns Response.success(MovieDtoMother.createNowPlayingMovieList())
		coEvery {
			flickSlateService.getUpcomingMovies(any(), any(), any())
		} returns Response.success(MovieDtoMother.createUpcomingMovieList())
		coEvery { popularMoviesDataSource.insertPopularMovies(any(), any()) } returns Unit
		coEvery { popularMoviesDataSource.insertPopularMoviesPageData(any()) } returns Unit
		coEvery { popularMoviesDataSource.getPopularMovies(any()) } returns flowOf(null)
		coEvery { upcomingMoviesDataSource.insertUpcomingMovies(any(), any()) } returns Unit
		coEvery { upcomingMoviesDataSource.insertUpcomingMoviesPageData(any()) } returns Unit
		coEvery { upcomingMoviesDataSource.getUpcomingMovies(any()) } returns flowOf(null)
		coEvery { nowPlayingMoviesDataSource.insertNowPlayingMovies(any(), any()) } returns Unit
		coEvery { nowPlayingMoviesDataSource.insertNowPlayingMoviesPageData(any()) } returns Unit
		coEvery { nowPlayingMoviesDataSource.getNowPlayingMovies(any()) } returns flowOf(null)
		moviesAccessor = MoviesAccessor(
			flickSlateService,
			popularMoviesDataSource,
			nowPlayingMoviesDataSource,
			upcomingMoviesDataSource
		)
	}

	@Test
	fun `when getPopularMovies called then returns correct result`() = runTest {
		val popularMoviesFlow = moviesAccessor.getPopularMovies(1)
		val pagingData = MovieMother.createPopularMovieList()
		popularMoviesFlow.first().value.pagingList shouldBeEqual pagingData
	}

	@Test
	fun `when getUpcomingMovies called then returns correct result`() = runTest {
		val upcomingMoviesFlow = moviesAccessor.getUpcomingMovies(1)
		val pagingData = MovieMother.createUpcomingMovieList()
		upcomingMoviesFlow.first().value.pagingList shouldBeEqual pagingData
	}

	@Test
	fun `when getNowPlayingMovies called then returns correct result`() = runTest {
		val nowPlayingMoviesFlow = moviesAccessor.getNowPlayingMovies(1)
		val pagingData = MovieMother.createNowPlayingMovieList()
		nowPlayingMoviesFlow.first().value.pagingList shouldBeEqual pagingData
	}

}
