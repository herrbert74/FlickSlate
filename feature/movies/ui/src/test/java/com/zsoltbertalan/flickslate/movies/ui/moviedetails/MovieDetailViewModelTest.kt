package com.zsoltbertalan.flickslate.movies.ui.moviedetails

import androidx.lifecycle.SavedStateHandle
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.account.domain.usecase.GetSessionIdUseCase
import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetailWithImages
import com.zsoltbertalan.flickslate.movies.domain.usecase.MovieDetailsUseCase
import com.zsoltbertalan.flickslate.movies.domain.usecase.RateMovieUseCase
import com.zsoltbertalan.flickslate.shared.kotlin.result.Failure
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MovieDetailViewModelTest {

	private val movieDetailsUseCase: MovieDetailsUseCase = mockk()
	private val rateMovieUseCase: RateMovieUseCase = mockk()
	private val getSessionIdUseCase: GetSessionIdUseCase = mockk()
	private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)

	private lateinit var viewModel: MovieDetailViewModel

	private val dispatcher = StandardTestDispatcher()

	@Before
	fun setUp() {
		Dispatchers.setMain(dispatcher)
		coEvery { savedStateHandle.get<Int>("movieId") } returns 1
		coEvery { getSessionIdUseCase.execute() } returns Ok("session-id")
	}

	@After
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun `when view model is initialized, movie details are fetched and logged in status is checked`() = runTest {
		val movieDetail = MovieDetailWithImages(id = 1, title = "Test Movie")
		coEvery { movieDetailsUseCase.getMovieDetails(1) } returns Ok(movieDetail)

		viewModel = MovieDetailViewModel(savedStateHandle, movieDetailsUseCase, rateMovieUseCase, getSessionIdUseCase)
		advanceUntilIdle()

		viewModel.movieStateData.value.movieDetail shouldBe movieDetail
		viewModel.movieStateData.value.isLoggedIn shouldBe true
	}

	@Test
	fun `when movie details fetch fails, state reflects error`() = runTest {
		coEvery { movieDetailsUseCase.getMovieDetails(1) } returns Err(Failure.UnknownHostFailure)

		viewModel = MovieDetailViewModel(savedStateHandle, movieDetailsUseCase, rateMovieUseCase, getSessionIdUseCase)
		advanceUntilIdle()

		viewModel.movieStateData.value.failure shouldBe Failure.UnknownHostFailure
		viewModel.movieStateData.value.movieDetail shouldBe null
	}

	@Test
	fun `when user is not logged in, state reflects it`() = runTest {
		val movieDetail = MovieDetailWithImages(id = 1, title = "Test Movie")
		coEvery { movieDetailsUseCase.getMovieDetails(1) } returns Ok(movieDetail)
		coEvery { getSessionIdUseCase.execute() } returns Err(Failure.UnknownHostFailure)

		viewModel = MovieDetailViewModel(savedStateHandle, movieDetailsUseCase, rateMovieUseCase, getSessionIdUseCase)
		advanceUntilIdle()

		viewModel.movieStateData.value.isLoggedIn shouldBe false
	}

	@Test
	fun `when movie is rated, state is updated`() = runTest {
		val movieDetail = MovieDetailWithImages(id = 1, title = "Test Movie", personalRating = 0.0f)
		coEvery { movieDetailsUseCase.getMovieDetails(1) } returns Ok(movieDetail)
		coEvery { rateMovieUseCase.execute(1, 8.0f) } returns Ok(Unit)

		viewModel = MovieDetailViewModel(savedStateHandle, movieDetailsUseCase, rateMovieUseCase, getSessionIdUseCase)
		advanceUntilIdle()
		viewModel.rateMovie(8.0f)
		advanceUntilIdle()

		viewModel.movieStateData.value.isRated shouldBe true
	}

	@Test
	fun `when movie is already rated, state reflects it`() = runTest {
		val movieDetail = MovieDetailWithImages(id = 1, title = "Test Movie", personalRating = 7.0f)
		coEvery { movieDetailsUseCase.getMovieDetails(1) } returns Ok(movieDetail)

		viewModel = MovieDetailViewModel(savedStateHandle, movieDetailsUseCase, rateMovieUseCase, getSessionIdUseCase)
		advanceUntilIdle()

		viewModel.movieStateData.value.isRated shouldBe true
	}

	@Test
	fun `when movie rating fails, state reflects error`() = runTest {
		val movieDetail = MovieDetailWithImages(id = 1, title = "Test Movie")
		coEvery { movieDetailsUseCase.getMovieDetails(1) } returns Ok(movieDetail)
		coEvery { rateMovieUseCase.execute(1, 8.0f) } returns Err(Failure.UnknownHostFailure)

		viewModel = MovieDetailViewModel(savedStateHandle, movieDetailsUseCase, rateMovieUseCase, getSessionIdUseCase)
		advanceUntilIdle()
		viewModel.rateMovie(8.0f)
		advanceUntilIdle()

		viewModel.movieStateData.value.failure shouldBe Failure.UnknownHostFailure
		viewModel.movieStateData.value.isRated shouldBe false
	}
}
