package com.zsoltbertalan.flickslate.tv.ui.seasondetail

import androidx.lifecycle.SavedStateHandle
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.account.domain.usecase.GetSessionIdUseCase
import com.zsoltbertalan.flickslate.shared.kotlin.result.Failure
import com.zsoltbertalan.flickslate.tv.domain.model.SeasonDetail
import com.zsoltbertalan.flickslate.tv.domain.model.TvMother
import com.zsoltbertalan.flickslate.tv.domain.usecase.ChangeTvShowEpisodeRatingUseCase
import com.zsoltbertalan.flickslate.tv.domain.usecase.DeleteTvShowEpisodeRatingUseCase
import com.zsoltbertalan.flickslate.tv.domain.usecase.GetEpisodeDetailUseCase
import com.zsoltbertalan.flickslate.tv.domain.usecase.GetSeasonDetailUseCase
import com.zsoltbertalan.flickslate.tv.domain.usecase.RateTvShowEpisodeUseCase
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class TvSeasonDetailViewModelTest {

	private val getSeasonDetailUseCase = mockk<GetSeasonDetailUseCase>()
	private val rateEpisodeUseCase = mockk<RateTvShowEpisodeUseCase>()
	private val changeEpisodeRatingUseCase = mockk<ChangeTvShowEpisodeRatingUseCase>()
	private val deleteEpisodeRatingUseCase = mockk<DeleteTvShowEpisodeRatingUseCase>()
	private val getSessionIdUseCase = mockk<GetSessionIdUseCase>()
	private val getEpisodeDetailUseCase = mockk<GetEpisodeDetailUseCase>()
	private val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)

	private lateinit var viewModel: TvSeasonDetailViewModel
	private lateinit var mockSeasonDetail: SeasonDetail

	private val dispatcher = StandardTestDispatcher()

	private val testSeriesId = 123
	private val testSeasonNumber = 1
	private val testBgColor = 1
	private val testBgColorDim = 2
	private val testSeasonTitle = "Season 1"
	private val testEpisodeId1 = 101
	private val testEpisodeId2 = 102

	@Before
	fun setUp() {
		Dispatchers.setMain(dispatcher)
		every { savedStateHandle.get<Int>(SERIES_ID_ARG) } returns testSeriesId
		every { savedStateHandle.get<Int>(SEASON_NUMBER_ARG) } returns testSeasonNumber
		every { savedStateHandle.get<Int>(BG_COLOR_ARG) } returns testBgColor
		every { savedStateHandle.get<Int>(BG_COLOR_DIM_ARG) } returns testBgColorDim
		every { savedStateHandle.get<String>(SEASON_TITLE_ARG) } returns testSeasonTitle

		mockSeasonDetail = TvMother.createSeasonDetail(
			seriesId = testSeriesId,
			seasonNumber = testSeasonNumber,
		)
		coEvery { getSeasonDetailUseCase.execute(testSeriesId, testSeasonNumber) } returns Ok(mockSeasonDetail)
		coEvery { getSessionIdUseCase.execute() } returns Ok("session")
		coEvery { rateEpisodeUseCase.execute(any(), any(), any(), any()) } returns Ok(Unit)
		coEvery { changeEpisodeRatingUseCase.execute(any(), any(), any(), any()) } returns Ok(Unit)
		coEvery { deleteEpisodeRatingUseCase.execute(any(), any(), any()) } returns Ok(Unit)
	}

	@After
	fun tearDown() {
		Dispatchers.resetMain()
	}

	private fun TestScope.initializeViewModel() {
		viewModel = TvSeasonDetailViewModel(
			getSeasonDetailUseCase,
			rateEpisodeUseCase,
			changeEpisodeRatingUseCase,
			deleteEpisodeRatingUseCase,
			getSessionIdUseCase,
			getEpisodeDetailUseCase,
			savedStateHandle
		)
		advanceUntilIdle()
	}

	@Test
	fun `when viewModel initialized then fetchSeasonDetails is called and returns correct data on success`() = runTest {
		initializeViewModel()

		val job = launch(UnconfinedTestDispatcher(testScheduler)) {
			val expectedState = TvSeasonDetailUiState(
				isLoading = false,
				title = testSeasonTitle,
				seasonDetail = mockSeasonDetail,
				bgColor = testBgColor,
				bgColorDim = testBgColorDim,
				failure = null,
				isLoggedIn = true,
				expandedEpisodeId = null
			)
			viewModel.uiState.value shouldBe expectedState
		}
		job.cancel()
	}

	@Test
	fun `when viewModel initialized and fetchSeasonDetails fails then returns proper failure`() = runTest {
		coEvery {
			getSeasonDetailUseCase.execute(
				testSeriesId,
				testSeasonNumber
			)
		} returns Err(Failure.UnknownHostFailure)
		coEvery { getSessionIdUseCase.execute() } returns Ok("session")
		initializeViewModel()

		val job = launch(UnconfinedTestDispatcher(testScheduler)) {
			val expectedState = TvSeasonDetailUiState(
				isLoading = false,
				title = testSeasonTitle,
				seasonDetail = null,
				bgColor = testBgColor,
				bgColorDim = testBgColorDim,
				failure = Failure.UnknownHostFailure,
				isLoggedIn = true,
				expandedEpisodeId = null
			)
			viewModel.uiState.value shouldBe expectedState
		}
		job.cancel()
	}

	@Test
	fun `when fetchSeasonDetails called explicitly then state updates correctly on success`() = runTest {
		initializeViewModel()

		viewModel.fetchSeasonDetails()

		viewModel.uiState.value.isLoading shouldBe true
		viewModel.uiState.value.failure shouldBe null

		advanceUntilIdle()

		val job = launch(UnconfinedTestDispatcher(testScheduler)) {
			val expectedState = TvSeasonDetailUiState(
				isLoading = false,
				title = testSeasonTitle,
				seasonDetail = mockSeasonDetail,
				bgColor = testBgColor,
				bgColorDim = testBgColorDim,
				failure = null,
				isLoggedIn = true,
				expandedEpisodeId = null
			)
			viewModel.uiState.value shouldBe expectedState
		}
		job.cancel()
	}

	@Test
	fun `when required args are missing from SavedStateHandle then viewModel init throws IllegalStateException`() =
		runTest {
			every { savedStateHandle.get<Int>(SERIES_ID_ARG) } returns null
			shouldThrowExactly<IllegalStateException> {
				TvSeasonDetailViewModel(
					getSeasonDetailUseCase,
					rateEpisodeUseCase,
					changeEpisodeRatingUseCase,
					deleteEpisodeRatingUseCase,
					getSessionIdUseCase,
					getEpisodeDetailUseCase,
					savedStateHandle
				)
			}

			every { savedStateHandle.get<Int>(SERIES_ID_ARG) } returns testSeriesId
			every { savedStateHandle.get<Int>(SEASON_NUMBER_ARG) } returns null
			shouldThrowExactly<IllegalStateException> {
				TvSeasonDetailViewModel(
					getSeasonDetailUseCase,
					rateEpisodeUseCase,
					changeEpisodeRatingUseCase,
					deleteEpisodeRatingUseCase,
					getSessionIdUseCase,
					getEpisodeDetailUseCase,
					savedStateHandle
				)
			}
		}

	@Test
	fun `initially expandedEpisodeId is null`() = runTest {
		initializeViewModel()
		viewModel.uiState.value.expandedEpisodeId shouldBe null
	}

	@Test
	fun `toggleEpisodeExpanded sets expandedEpisodeId when none is expanded`() = runTest {
		initializeViewModel()
		viewModel.toggleEpisodeExpanded(testEpisodeId1)
		viewModel.uiState.value.expandedEpisodeId shouldBe testEpisodeId1
	}

	@Test
	fun `toggleEpisodeExpanded clears expandedEpisodeId when same episode is toggled`() = runTest {
		initializeViewModel()
		viewModel.toggleEpisodeExpanded(testEpisodeId1)
		viewModel.toggleEpisodeExpanded(testEpisodeId1)
		viewModel.uiState.value.expandedEpisodeId shouldBe null
	}

	@Test
	fun `toggleEpisodeExpanded changes expandedEpisodeId when different episode is toggled`() = runTest {
		initializeViewModel()
		viewModel.toggleEpisodeExpanded(testEpisodeId1)
		viewModel.toggleEpisodeExpanded(testEpisodeId2)
		viewModel.uiState.value.expandedEpisodeId shouldBe testEpisodeId2
	}

	@Test
	fun `toggleEpisodeExpanded does not affect other UI state properties`() = runTest {
		initializeViewModel()
		val initialState = viewModel.uiState.value

		viewModel.toggleEpisodeExpanded(testEpisodeId1)

		val stateAfterToggle = viewModel.uiState.value
		stateAfterToggle.copy(expandedEpisodeId = initialState.expandedEpisodeId) shouldBe initialState
	}

	@Test
	fun `checkLoginStatus updates isLoggedIn when session available`() = runTest {
		coEvery { getSessionIdUseCase.execute() } returns Ok("session")
		coEvery { getSeasonDetailUseCase.execute(testSeriesId, testSeasonNumber) } returns Ok(mockSeasonDetail)
		initializeViewModel()
		viewModel.uiState.value.isLoggedIn shouldBe true
	}

	@Test
	fun `checkLoginStatus false when session missing`() = runTest {
		coEvery { getSessionIdUseCase.execute() } returns Err(Failure.UserNotLoggedIn)
		coEvery { getSeasonDetailUseCase.execute(testSeriesId, testSeasonNumber) } returns Ok(mockSeasonDetail)
		initializeViewModel()
		viewModel.uiState.value.isLoggedIn shouldBe false
	}

	@Test
	fun `rateEpisode success updates toast and flags`() = runTest {
		coEvery { getSeasonDetailUseCase.execute(testSeriesId, testSeasonNumber) } returns Ok(mockSeasonDetail)
		coEvery { getSessionIdUseCase.execute() } returns Ok("session")
		coEvery { rateEpisodeUseCase.execute(testSeriesId, testSeasonNumber, any(), any()) } returns Ok(Unit)
		initializeViewModel()

		viewModel.rateEpisode(episodeNumber = mockSeasonDetail.episodes.first().episodeNumber, rating = 7.5f)

		advanceUntilIdle()
		viewModel.uiState.value.isRatingInProgress shouldBe false
		viewModel.uiState.value.isRated shouldBe true
		viewModel.uiState.value.showRatingToast shouldBe true
	}

	@Test
	fun `rateEpisode failure sets failure`() = runTest {
		coEvery { getSeasonDetailUseCase.execute(testSeriesId, testSeasonNumber) } returns Ok(mockSeasonDetail)
		coEvery { getSessionIdUseCase.execute() } returns Ok("session")
		coEvery {
			rateEpisodeUseCase.execute(
				testSeriesId,
				testSeasonNumber,
				any(),
				any()
			)
		} returns Err(Failure.ServerError("boom"))
		initializeViewModel()

		viewModel.rateEpisode(mockSeasonDetail.episodes.first().episodeNumber, 5f)
		advanceUntilIdle()
		viewModel.uiState.value.failure shouldBe Failure.ServerError("boom")
	}

	@Test
	fun `changeEpisodeRating success updates toast`() = runTest {
		initializeViewModel()

		viewModel.changeEpisodeRating(mockSeasonDetail.episodes.first().episodeNumber, 8.5f)
		advanceUntilIdle()

		with(viewModel.uiState.value) {
			isRatingInProgress shouldBe false
			showRatingToast shouldBe true
		}
	}

	@Test
	fun `changeEpisodeRating failure sets failure`() = runTest {
		coEvery { changeEpisodeRatingUseCase.execute(any(), any(), any(), any()) } returns Err(Failure.ServerError("boom"))
		initializeViewModel()

		viewModel.changeEpisodeRating(mockSeasonDetail.episodes.first().episodeNumber, 8.5f)
		advanceUntilIdle()

		viewModel.uiState.value.failure shouldBe Failure.ServerError("boom")
	}

	@Test
	fun `deleteEpisodeRating success updates toast`() = runTest {
		initializeViewModel()

		viewModel.deleteEpisodeRating(mockSeasonDetail.episodes.first().episodeNumber)
		advanceUntilIdle()

		with(viewModel.uiState.value) {
			isRatingInProgress shouldBe false
			showRatingToast shouldBe true
		}
	}

	@Test
	fun `deleteEpisodeRating failure sets failure`() = runTest {
		coEvery { deleteEpisodeRatingUseCase.execute(any(), any(), any()) } returns Err(Failure.ServerError("boom"))
		initializeViewModel()

		viewModel.deleteEpisodeRating(mockSeasonDetail.episodes.first().episodeNumber)
		advanceUntilIdle()

		viewModel.uiState.value.failure shouldBe Failure.ServerError("boom")
	}

	@Test
	fun `toggleEpisodeExpanded fetches episode detail and updates personal rating`() = runTest {
		coEvery { getSeasonDetailUseCase.execute(testSeriesId, testSeasonNumber) } returns Ok(mockSeasonDetail)
		coEvery { getSessionIdUseCase.execute() } returns Ok("session")
		val updatedDetail = mockSeasonDetail.episodes.first().copy(personalRating = 9f)
		coEvery { getEpisodeDetailUseCase.execute(any(), any(), any()) } returns Ok(updatedDetail)
		initializeViewModel()

		viewModel.toggleEpisodeExpanded(mockSeasonDetail.episodes.first().id)
		advanceUntilIdle()
		viewModel.uiState.value.seasonDetail?.episodes?.first()?.personalRating shouldBe 9f
	}

	@Test
	fun `toastShown resets flag`() = runTest {
		coEvery { getSeasonDetailUseCase.execute(testSeriesId, testSeasonNumber) } returns Ok(mockSeasonDetail)
		coEvery { getSessionIdUseCase.execute() } returns Ok("session")
		initializeViewModel()
		viewModel.toastShown()
		viewModel.uiState.value.showRatingToast shouldBe false
	}

}
