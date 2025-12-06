package com.zsoltbertalan.flickslate.tv.ui.tvdetail

import androidx.lifecycle.SavedStateHandle
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.account.domain.usecase.GetSessionIdUseCase
import com.zsoltbertalan.flickslate.shared.kotlin.result.Failure
import com.zsoltbertalan.flickslate.tv.domain.model.TvMother
import com.zsoltbertalan.flickslate.tv.domain.usecase.ChangeTvRatingUseCase
import com.zsoltbertalan.flickslate.tv.domain.usecase.DeleteTvRatingUseCase
import com.zsoltbertalan.flickslate.tv.domain.usecase.RateTvShowUseCase
import com.zsoltbertalan.flickslate.tv.domain.usecase.TvDetailsUseCase
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
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
class TvDetailViewModelTest {

	private val tvDetailsUseCase = mockk<TvDetailsUseCase>()
	private val rateTvShowUseCase = mockk<RateTvShowUseCase>()
	private val changeTvRatingUseCase = mockk<ChangeTvRatingUseCase>()
	private val deleteTvRatingUseCase = mockk<DeleteTvRatingUseCase>()
	private val getSessionIdUseCase = mockk<GetSessionIdUseCase>()
	private val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)

	private lateinit var viewModel: TvDetailViewModel

	private val dispatcher = StandardTestDispatcher()

	private val testSeriesId = 123

	@Before
	fun setUp() {
		Dispatchers.setMain(dispatcher)
		every { savedStateHandle.get<Int>(SERIES_ID_ARG) } returns testSeriesId
		every { savedStateHandle.get<Int>(SEASON_NUMBER_ARG) } returns null
		every { savedStateHandle.get<Int>(EPISODE_NUMBER_ARG) } returns null
	}

	@After
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun `when viewModel initialized then getTvDetails is called and returns correct data on success`() = runTest {
		val mockTvDetail = TvMother.createTvDetailWithImages(id = testSeriesId)
		coEvery { tvDetailsUseCase.getTvDetails(testSeriesId) } returns Ok(mockTvDetail)
		coEvery { getSessionIdUseCase.execute() } returns Ok("session")

		viewModel = TvDetailViewModel(
			savedStateHandle,
			tvDetailsUseCase,
			rateTvShowUseCase,
			changeTvRatingUseCase,
			deleteTvRatingUseCase,
			getSessionIdUseCase
		)

		advanceUntilIdle()

		viewModel.tvStateData.value shouldBe TvDetailState(
			tvDetail = mockTvDetail,
			failure = null,
			seasonNumber = null,
			episodeNumber = null,
			isLoggedIn = true
		)
	}

	@Test
	fun `when viewModel initialized and getTvDetails fails then returns proper failure`() = runTest {
		val mockFailure = Failure.UnknownHostFailure
		coEvery { tvDetailsUseCase.getTvDetails(testSeriesId) } returns Err(mockFailure)
		coEvery { getSessionIdUseCase.execute() } returns Err(Failure.UserNotLoggedIn)

		viewModel = TvDetailViewModel(
			savedStateHandle,
			tvDetailsUseCase,
			rateTvShowUseCase,
			changeTvRatingUseCase,
			deleteTvRatingUseCase,
			getSessionIdUseCase
		)

		advanceUntilIdle()

		viewModel.tvStateData.value shouldBe TvDetailState(
			tvDetail = null,
			failure = mockFailure,
			seasonNumber = null,
			episodeNumber = null,
			isLoggedIn = false
		)
	}

	@Test
	fun `when seriesId is not available in SavedStateHandle then viewModel init throws IllegalStateException`() =
		runTest {
			every { savedStateHandle.get<Int>(SERIES_ID_ARG) } returns null

			shouldThrowExactly<IllegalStateException> {
				TvDetailViewModel(
					savedStateHandle,
					tvDetailsUseCase,
					rateTvShowUseCase,
					changeTvRatingUseCase,
					deleteTvRatingUseCase,
					getSessionIdUseCase
				)
			}
		}

	@Test
	fun `when viewModel initialized and tv is already rated, state reflects it`() = runTest {
		val mockTvDetail = TvMother.createTvDetailWithImages(id = testSeriesId, personalRating = 8.0f)
		coEvery { tvDetailsUseCase.getTvDetails(testSeriesId) } returns Ok(mockTvDetail)
		coEvery { getSessionIdUseCase.execute() } returns Ok("session")

		viewModel = TvDetailViewModel(
			savedStateHandle,
			tvDetailsUseCase,
			rateTvShowUseCase,
			changeTvRatingUseCase,
			deleteTvRatingUseCase,
			getSessionIdUseCase
		)
		advanceUntilIdle()

		viewModel.tvStateData.value.isRated shouldBe true
	}

	@Test
	fun `rateTvShow success updates state`() = runTest {
		val mockTvDetail = TvMother.createTvDetailWithImages(id = testSeriesId)
		coEvery { tvDetailsUseCase.getTvDetails(testSeriesId) } returns Ok(mockTvDetail)
		coEvery { getSessionIdUseCase.execute() } returns Ok("session")
		coEvery { rateTvShowUseCase.execute(testSeriesId, any()) } returns Ok(Unit)

		viewModel = TvDetailViewModel(
			savedStateHandle,
			tvDetailsUseCase,
			rateTvShowUseCase,
			changeTvRatingUseCase,
			deleteTvRatingUseCase,
			getSessionIdUseCase
		)
		advanceUntilIdle()

		viewModel.rateTvShow(8.0f)
		advanceUntilIdle()

		viewModel.tvStateData.value.isRated shouldBe true
		viewModel.tvStateData.value.showRatingToast shouldBe true
		viewModel.tvStateData.value.tvDetail?.personalRating shouldBe 8.0f
	}

	@Test
	fun `rateTvShow failure sets failure`() = runTest {
		val mockTvDetail = TvMother.createTvDetailWithImages(id = testSeriesId)
		coEvery { tvDetailsUseCase.getTvDetails(testSeriesId) } returns Ok(mockTvDetail)
		coEvery { getSessionIdUseCase.execute() } returns Ok("session")
		coEvery { rateTvShowUseCase.execute(testSeriesId, any()) } returns Err(Failure.ServerError("boom"))

		viewModel = TvDetailViewModel(
			savedStateHandle,
			tvDetailsUseCase,
			rateTvShowUseCase,
			changeTvRatingUseCase,
			deleteTvRatingUseCase,
			getSessionIdUseCase
		)
		advanceUntilIdle()

		viewModel.rateTvShow(5f)
		advanceUntilIdle()

		viewModel.tvStateData.value.failure shouldBe Failure.ServerError("boom")
		viewModel.tvStateData.value.isRatingInProgress shouldBe false
	}

	@Test
	fun `change rating success updates state`() = runTest {
		val mockTvDetail = TvMother.createTvDetailWithImages(id = testSeriesId, personalRating = 7.0f)
		coEvery { tvDetailsUseCase.getTvDetails(testSeriesId) } returns Ok(mockTvDetail)
		coEvery { getSessionIdUseCase.execute() } returns Ok("session")
		coEvery { changeTvRatingUseCase.execute(testSeriesId, 9.0f) } returns Ok(Unit)

		viewModel = TvDetailViewModel(
			savedStateHandle,
			tvDetailsUseCase,
			rateTvShowUseCase,
			changeTvRatingUseCase,
			deleteTvRatingUseCase,
			getSessionIdUseCase
		)
		advanceUntilIdle()

		viewModel.changeTvRating(9.0f)
		advanceUntilIdle()

		viewModel.tvStateData.value.tvDetail?.personalRating shouldBe 9.0f
		viewModel.tvStateData.value.showRatingToast shouldBe true
	}

	@Test
	fun `change rating failure sets failure`() = runTest {
		val mockTvDetail = TvMother.createTvDetailWithImages(id = testSeriesId, personalRating = 7.0f)
		coEvery { tvDetailsUseCase.getTvDetails(testSeriesId) } returns Ok(mockTvDetail)
		coEvery { getSessionIdUseCase.execute() } returns Ok("session")
		coEvery { changeTvRatingUseCase.execute(testSeriesId, 9.0f) } returns Err(Failure.UnknownHostFailure)

		viewModel = TvDetailViewModel(
			savedStateHandle,
			tvDetailsUseCase,
			rateTvShowUseCase,
			changeTvRatingUseCase,
			deleteTvRatingUseCase,
			getSessionIdUseCase
		)
		advanceUntilIdle()

		viewModel.changeTvRating(9.0f)
		advanceUntilIdle()

		viewModel.tvStateData.value.failure shouldBe Failure.UnknownHostFailure
		viewModel.tvStateData.value.isRatingInProgress shouldBe false
	}

	@Test
	fun `delete rating success clears state`() = runTest {
		val mockTvDetail = TvMother.createTvDetailWithImages(id = testSeriesId, personalRating = 6.0f)
		coEvery { tvDetailsUseCase.getTvDetails(testSeriesId) } returns Ok(mockTvDetail)
		coEvery { getSessionIdUseCase.execute() } returns Ok("session")
		coEvery { deleteTvRatingUseCase.execute(testSeriesId) } returns Ok(Unit)

		viewModel = TvDetailViewModel(
			savedStateHandle,
			tvDetailsUseCase,
			rateTvShowUseCase,
			changeTvRatingUseCase,
			deleteTvRatingUseCase,
			getSessionIdUseCase
		)
		advanceUntilIdle()

		viewModel.deleteTvRating()
		advanceUntilIdle()

		viewModel.tvStateData.value.isRated shouldBe false
		viewModel.tvStateData.value.tvDetail?.personalRating shouldBe -1f
	}

	@Test
	fun `delete rating failure sets failure`() = runTest {
		val mockTvDetail = TvMother.createTvDetailWithImages(id = testSeriesId, personalRating = 6.0f)
		coEvery { tvDetailsUseCase.getTvDetails(testSeriesId) } returns Ok(mockTvDetail)
		coEvery { getSessionIdUseCase.execute() } returns Ok("session")
		coEvery { deleteTvRatingUseCase.execute(testSeriesId) } returns Err(Failure.UnknownHostFailure)

		viewModel = TvDetailViewModel(
			savedStateHandle,
			tvDetailsUseCase,
			rateTvShowUseCase,
			changeTvRatingUseCase,
			deleteTvRatingUseCase,
			getSessionIdUseCase
		)
		advanceUntilIdle()

		viewModel.deleteTvRating()
		advanceUntilIdle()

		viewModel.tvStateData.value.failure shouldBe Failure.UnknownHostFailure
		viewModel.tvStateData.value.isRatingInProgress shouldBe false
	}

}
