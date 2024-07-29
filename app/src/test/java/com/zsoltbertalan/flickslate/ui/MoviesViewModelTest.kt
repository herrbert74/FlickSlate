package com.zsoltbertalan.flickslate.presentation.ui

import androidx.paging.PagingData
import com.zsoltbertalan.flickslate.domain.api.MoviesRepository
import com.zsoltbertalan.flickslate.domain.model.Movie
import com.zsoltbertalan.flickslate.common.testhelper.MovieMother
import com.zsoltbertalan.flickslate.presentation.ui.movies.MoviesViewModel
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class MoviesViewModelTest {

	private val moviesRepository = mockk<MoviesRepository>(relaxed = false)

	private lateinit var moviesViewModel: MoviesViewModel

	private val dispatcher = StandardTestDispatcher()

	private lateinit var pagingData: PagingData<Movie>

	@Before
	fun setUp() {
		pagingData = PagingData.from(
			MovieMother.createMovieList()
		)
		Dispatchers.setMain(dispatcher)
		coEvery { moviesRepository.getPopularMovies(any()) } answers { flowOf(pagingData) }
		coEvery { moviesRepository.getUpcomingMovies(any()) } answers { flowOf(pagingData) }
		coEvery { moviesRepository.getNowPlayingMovies(any()) } answers { flowOf(pagingData) }

		moviesViewModel = MoviesViewModel(moviesRepository)
	}

	@After
	fun tearDown() {

		Dispatchers.resetMain()

	}

	@Test
	fun `when started then getMovies is called and returns correct list`() = runTest {

		coVerify(exactly = 1) { moviesRepository.getPopularMovies(any()) }

		moviesViewModel.popularMoviesList.first() shouldBeEqualToComparingFields pagingData

	}

}


