package com.zsoltbertalan.flickslate.tv.ui.tvdetail

import androidx.lifecycle.SavedStateHandle
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.shared.kotlin.result.Failure
import com.zsoltbertalan.flickslate.tv.domain.model.TvMother
import com.zsoltbertalan.flickslate.tv.domain.usecase.TvDetailsUseCase
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@ExperimentalCoroutinesApi
class TvDetailViewModelTest {

	private val tvDetailsUseCase = mockk<TvDetailsUseCase>()
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

		viewModel = TvDetailViewModel(savedStateHandle, tvDetailsUseCase)

		advanceUntilIdle()

		val job = launch(UnconfinedTestDispatcher(testScheduler)) {
			val expectedState = TvDetailState(tvDetail = mockTvDetail, failure = null)
			viewModel.tvStateData.value shouldBe expectedState
		}
		job.cancel()
	}

	@Test
	fun `when viewModel initialized and getTvDetails fails then returns proper failure`() = runTest {
		val mockFailure = Failure.UnknownHostFailure
		coEvery { tvDetailsUseCase.getTvDetails(testSeriesId) } returns Err(mockFailure)

		viewModel = TvDetailViewModel(savedStateHandle, tvDetailsUseCase)

		advanceUntilIdle()

		val job = launch(UnconfinedTestDispatcher(testScheduler)) {
			val expectedState = TvDetailState(tvDetail = null, failure = mockFailure)
			viewModel.tvStateData.value shouldBe expectedState
		}
		job.cancel()
	}

	@Test
	fun `when seriesId is not available in SavedStateHandle then viewModel init throws IllegalStateException`() = runTest {
		every { savedStateHandle.get<Int>(SERIES_ID_ARG) } returns null

		shouldThrowExactly<IllegalStateException> { TvDetailViewModel(savedStateHandle, tvDetailsUseCase) }
	}

}
