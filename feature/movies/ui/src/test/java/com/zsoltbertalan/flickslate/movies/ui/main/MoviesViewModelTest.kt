package com.zsoltbertalan.flickslate.movies.ui.main

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.account.domain.api.AccountRepository
import com.zsoltbertalan.flickslate.account.domain.model.AccountMother
import com.zsoltbertalan.flickslate.base.kotlin.result.Failure
import com.zsoltbertalan.flickslate.movies.domain.api.MoviesRepository
import com.zsoltbertalan.flickslate.shared.domain.model.MovieMother
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.ui.compose.component.paging.PaginationInternalState
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class MoviesViewModelTest {

	private val moviesRepository = mockk<MoviesRepository>(relaxed = false)
	private val accountRepository = mockk<AccountRepository>(relaxed = true)

	private lateinit var moviesViewModel: MoviesViewModel

	private val dispatcher = StandardTestDispatcher()

	@Before
	fun setUp() {
		Dispatchers.setMain(dispatcher)

		coEvery { moviesRepository.getPopularMovies(any()) } answers {
			flowOf(Ok(PagingReply(MovieMother.createPopularMovieList(), true, PageData())))
		}
		coEvery { moviesRepository.getUpcomingMovies(any(), any()) } answers {
			flowOf(Ok(PagingReply(MovieMother.createUpcomingMovieList(), true, PageData())))
		}
		coEvery { moviesRepository.getNowPlayingMovies(any(), any()) } answers {
			flowOf(Ok(PagingReply(MovieMother.createNowPlayingMovieList(), true, PageData())))
		}
		coEvery { accountRepository.getAccount() } returns AccountMother.createAccount()

		moviesViewModel = MoviesViewModel(moviesRepository, accountRepository)
	}

	@After
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun `when started then getMovies is called and returns correct list`() = runTest {
		moviesViewModel.popularMoviesPaginationState.onRequestPage(moviesViewModel.popularMoviesPaginationState, 1)

		advanceUntilIdle()
		coVerify(exactly = 1) { moviesRepository.getPopularMovies(any()) }

		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			moviesViewModel.popularMoviesPaginationState.allItems shouldBeEqual MovieMother.createPopularMovieList()
		}

	}

	@Test
	fun `when started and getMovies is called and call fails returns proper failure`() = runTest {
		coEvery { moviesRepository.getPopularMovies(any()) } answers {
			flowOf(Err(Failure.UnknownHostFailure))
		}
		moviesViewModel.popularMoviesPaginationState.onRequestPage(moviesViewModel.popularMoviesPaginationState, 1)

		advanceUntilIdle()
		coVerify(exactly = 1) { moviesRepository.getPopularMovies(any()) }

		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			shouldThrowExactly<NoSuchElementException> { moviesViewModel.popularMoviesPaginationState.allItems }
			moviesViewModel.popularMoviesPaginationState.internalState.value shouldBeEqualToComparingFields
				PaginationInternalState.Error(1, 1, Exception("Unknown host"), null)
		}

	}

	@Test
	fun `when user is logged in upcoming movies call uses user region`() = runTest {
		moviesViewModel.upcomingMoviesPaginationState.onRequestPage(moviesViewModel.upcomingMoviesPaginationState, 1)

		advanceUntilIdle()
		coVerify(exactly = 1) { moviesRepository.getUpcomingMovies(1, "GB") }
	}

	@Test
	fun `when user is logged in now playing movies call uses user region`() = runTest {
		moviesViewModel.nowPlayingMoviesPaginationState.onRequestPage(
			moviesViewModel.nowPlayingMoviesPaginationState,
			1
		)

		advanceUntilIdle()
		coVerify(exactly = 1) { moviesRepository.getNowPlayingMovies(1, "GB") }
	}

	@Test
	fun `when user is not logged in upcoming movies call uses US region`() = runTest {
		coEvery { accountRepository.getAccount() } returns null

		moviesViewModel.upcomingMoviesPaginationState.onRequestPage(moviesViewModel.upcomingMoviesPaginationState, 1)

		advanceUntilIdle()
		coVerify(exactly = 1) { moviesRepository.getUpcomingMovies(1, "US") }
	}

}
