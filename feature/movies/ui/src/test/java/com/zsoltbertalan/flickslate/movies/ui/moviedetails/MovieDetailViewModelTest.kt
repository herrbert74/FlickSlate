package com.zsoltbertalan.flickslate.movies.ui.moviedetails

import androidx.lifecycle.SavedStateHandle
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.account.domain.usecase.GetSessionIdUseCase
import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetailWithImages
import com.zsoltbertalan.flickslate.movies.domain.usecase.ChangeMovieRatingUseCase
import com.zsoltbertalan.flickslate.movies.domain.usecase.DeleteMovieRatingUseCase
import com.zsoltbertalan.flickslate.movies.domain.usecase.MovieDetailsUseCase
import com.zsoltbertalan.flickslate.movies.domain.usecase.RateMovieUseCase
import com.zsoltbertalan.flickslate.movies.domain.usecase.SetMovieFavoriteUseCase
import com.zsoltbertalan.flickslate.shared.kotlin.result.Failure
import com.zsoltbertalan.flickslate.shared.ui.compose.component.rating.RatingToastMessage
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
	private val changeMovieRatingUseCase: ChangeMovieRatingUseCase = mockk()
	private val deleteMovieRatingUseCase: DeleteMovieRatingUseCase = mockk()
	private val setMovieFavoriteUseCase: SetMovieFavoriteUseCase = mockk()
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

		viewModel = MovieDetailViewModel(
			savedStateHandle,
			movieDetailsUseCase,
			rateMovieUseCase,
			changeMovieRatingUseCase,
			deleteMovieRatingUseCase,
			setMovieFavoriteUseCase,
			getSessionIdUseCase
		)
		viewModel.load(1)
		advanceUntilIdle()

		viewModel.movieStateData.value.movieDetail shouldBe movieDetail
		viewModel.movieStateData.value.isLoggedIn shouldBe true
	}

	@Test
	fun `when movie details fetch fails, state reflects error`() = runTest {
		coEvery { movieDetailsUseCase.getMovieDetails(1) } returns Err(Failure.UnknownHostFailure)

		viewModel = MovieDetailViewModel(
			savedStateHandle,
			movieDetailsUseCase,
			rateMovieUseCase,
			changeMovieRatingUseCase,
			deleteMovieRatingUseCase,
			setMovieFavoriteUseCase,
			getSessionIdUseCase
		)
		viewModel.load(1)
		advanceUntilIdle()

		viewModel.movieStateData.value.failure shouldBe Failure.UnknownHostFailure
		viewModel.movieStateData.value.movieDetail shouldBe null
	}

	@Test
	fun `when user is not logged in, state reflects it`() = runTest {
		val movieDetail = MovieDetailWithImages(id = 1, title = "Test Movie")
		coEvery { movieDetailsUseCase.getMovieDetails(1) } returns Ok(movieDetail)
		coEvery { getSessionIdUseCase.execute() } returns Err(Failure.UnknownHostFailure)

		viewModel = MovieDetailViewModel(
			savedStateHandle,
			movieDetailsUseCase,
			rateMovieUseCase,
			changeMovieRatingUseCase,
			deleteMovieRatingUseCase,
			setMovieFavoriteUseCase,
			getSessionIdUseCase
		)
		viewModel.load(1)
		advanceUntilIdle()

		viewModel.movieStateData.value.isLoggedIn shouldBe false
	}

	@Test
	fun `when movie is rated, state is updated`() = runTest {
		val movieDetail = MovieDetailWithImages(id = 1, title = "Test Movie", personalRating = -1f)
		coEvery { movieDetailsUseCase.getMovieDetails(1) } returns Ok(movieDetail)
		coEvery { rateMovieUseCase.execute(1, 8.0f) } returns Ok(Unit)

		viewModel = MovieDetailViewModel(
			savedStateHandle,
			movieDetailsUseCase,
			rateMovieUseCase,
			changeMovieRatingUseCase,
			deleteMovieRatingUseCase,
			setMovieFavoriteUseCase,
			getSessionIdUseCase
		)
		viewModel.load(1)
		advanceUntilIdle()
		viewModel.rateMovie(8.0f)
		advanceUntilIdle()

		viewModel.movieStateData.value.isRated shouldBe true
		viewModel.movieStateData.value.lastRatedValue shouldBe 8.0f
	}

	@Test
	fun `when movie is already rated, state reflects it`() = runTest {
		val movieDetail = MovieDetailWithImages(id = 1, title = "Test Movie", personalRating = 7.0f)
		coEvery { movieDetailsUseCase.getMovieDetails(1) } returns Ok(movieDetail)

		viewModel = MovieDetailViewModel(
			savedStateHandle,
			movieDetailsUseCase,
			rateMovieUseCase,
			changeMovieRatingUseCase,
			deleteMovieRatingUseCase,
			setMovieFavoriteUseCase,
			getSessionIdUseCase
		)
		viewModel.load(1)
		advanceUntilIdle()

		viewModel.movieStateData.value.isRated shouldBe true
	}

	@Test
	fun `when movie rating fails, state reflects error`() = runTest {
		val movieDetail = MovieDetailWithImages(id = 1, title = "Test Movie")
		coEvery { movieDetailsUseCase.getMovieDetails(1) } returns Ok(movieDetail)
		coEvery { rateMovieUseCase.execute(1, 8.0f) } returns Err(Failure.UnknownHostFailure)

		viewModel = MovieDetailViewModel(
			savedStateHandle,
			movieDetailsUseCase,
			rateMovieUseCase,
			changeMovieRatingUseCase,
			deleteMovieRatingUseCase,
			setMovieFavoriteUseCase,
			getSessionIdUseCase
		)
		viewModel.load(1)
		advanceUntilIdle()
		viewModel.rateMovie(8.0f)
		advanceUntilIdle()

		viewModel.movieStateData.value.failure shouldBe Failure.UnknownHostFailure
		viewModel.movieStateData.value.isRated shouldBe false
	}

	@Test
	fun `when rating is changed successfully, state updates`() = runTest {
		val movieDetail = MovieDetailWithImages(id = 1, title = "Test Movie", personalRating = 6.0f)
		coEvery { movieDetailsUseCase.getMovieDetails(1) } returns Ok(movieDetail)
		coEvery { changeMovieRatingUseCase.execute(1, 9.0f) } returns Ok(Unit)

		viewModel = MovieDetailViewModel(
			savedStateHandle,
			movieDetailsUseCase,
			rateMovieUseCase,
			changeMovieRatingUseCase,
			deleteMovieRatingUseCase,
			setMovieFavoriteUseCase,
			getSessionIdUseCase
		)
		viewModel.load(1)
		advanceUntilIdle()
		viewModel.changeRating(9.0f)
		advanceUntilIdle()

		viewModel.movieStateData.value.movieDetail?.personalRating shouldBe 9.0f
		viewModel.movieStateData.value.ratingToastMessage shouldBe RatingToastMessage.Updated
	}

	@Test
	fun `when rating delete succeeds, state resets`() = runTest {
		val movieDetail = MovieDetailWithImages(id = 1, title = "Test Movie", personalRating = 7.0f)
		coEvery { movieDetailsUseCase.getMovieDetails(1) } returns Ok(movieDetail)
		coEvery { deleteMovieRatingUseCase.execute(1) } returns Ok(Unit)

		viewModel = MovieDetailViewModel(
			savedStateHandle,
			movieDetailsUseCase,
			rateMovieUseCase,
			changeMovieRatingUseCase,
			deleteMovieRatingUseCase,
			setMovieFavoriteUseCase,
			getSessionIdUseCase
		)
		viewModel.load(1)
		advanceUntilIdle()
		viewModel.deleteRating()
		advanceUntilIdle()

		viewModel.movieStateData.value.isRated shouldBe false
		viewModel.movieStateData.value.movieDetail?.personalRating shouldBe -1f
		viewModel.movieStateData.value.ratingToastMessage shouldBe RatingToastMessage.Deleted
	}

	@Test
	fun `when change rating fails, state reflects error`() = runTest {
		val movieDetail = MovieDetailWithImages(id = 1, title = "Test Movie", personalRating = 6.0f)
		coEvery { movieDetailsUseCase.getMovieDetails(1) } returns Ok(movieDetail)
		coEvery { changeMovieRatingUseCase.execute(1, 9.0f) } returns Err(Failure.UnknownHostFailure)

		viewModel = MovieDetailViewModel(
			savedStateHandle,
			movieDetailsUseCase,
			rateMovieUseCase,
			changeMovieRatingUseCase,
			deleteMovieRatingUseCase,
			setMovieFavoriteUseCase,
			getSessionIdUseCase
		)
		viewModel.load(1)
		advanceUntilIdle()
		viewModel.changeRating(9.0f)
		advanceUntilIdle()

		viewModel.movieStateData.value.failure shouldBe Failure.UnknownHostFailure
		viewModel.movieStateData.value.isRatingInProgress shouldBe false
		viewModel.movieStateData.value.ratingToastMessage shouldBe null
	}

	@Test
	fun `when delete rating fails, state reflects error`() = runTest {
		val movieDetail = MovieDetailWithImages(id = 1, title = "Test Movie", personalRating = 7.0f)
		coEvery { movieDetailsUseCase.getMovieDetails(1) } returns Ok(movieDetail)
		coEvery { deleteMovieRatingUseCase.execute(1) } returns Err(Failure.UnknownHostFailure)

		viewModel = MovieDetailViewModel(
			savedStateHandle,
			movieDetailsUseCase,
			rateMovieUseCase,
			changeMovieRatingUseCase,
			deleteMovieRatingUseCase,
			setMovieFavoriteUseCase,
			getSessionIdUseCase
		)
		viewModel.load(1)
		advanceUntilIdle()
		viewModel.deleteRating()
		advanceUntilIdle()

		viewModel.movieStateData.value.failure shouldBe Failure.UnknownHostFailure
		viewModel.movieStateData.value.isRatingInProgress shouldBe false
		viewModel.movieStateData.value.ratingToastMessage shouldBe null
	}
}
