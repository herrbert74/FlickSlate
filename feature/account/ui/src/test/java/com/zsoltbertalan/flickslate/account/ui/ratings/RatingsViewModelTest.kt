package com.zsoltbertalan.flickslate.account.ui.ratings

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.account.domain.usecase.GetRatedMoviesUseCase
import com.zsoltbertalan.flickslate.account.domain.usecase.GetRatedTvShowEpisodesUseCase
import com.zsoltbertalan.flickslate.account.domain.usecase.GetRatedTvShowsUseCase
import com.zsoltbertalan.flickslate.shared.kotlin.result.Failure
import com.zsoltbertalan.flickslate.tv.domain.model.TvMother
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.toImmutableList
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

@OptIn(ExperimentalCoroutinesApi::class)
class RatingsViewModelTest {

	private val getRatedMoviesUseCase: GetRatedMoviesUseCase = mockk()
	private val getRatedTvShowsUseCase: GetRatedTvShowsUseCase = mockk()
	private val getRatedTvShowEpisodesUseCase: GetRatedTvShowEpisodesUseCase = mockk()

	private lateinit var viewModel: RatingsViewModel
	private val dispatcher = StandardTestDispatcher()

	@Before
	fun setUp() {
		Dispatchers.setMain(dispatcher)
	}

	@After
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun `when viewmodel is created then success state is emitted`() = runTest {
		val movies = RatedMovieMother.createRatedMovieList()
		val tvShows = TvMother.createTvList()
		val tvEpisodes = listOf(TvMother.createSeasonDetail(1, 1).episodes.first())

		coEvery { getRatedMoviesUseCase.execute() } returns Ok(movies)
		coEvery { getRatedTvShowsUseCase.execute() } returns Ok(tvShows)
		coEvery { getRatedTvShowEpisodesUseCase.execute() } returns Ok(tvEpisodes)

		viewModel = RatingsViewModel(getRatedMoviesUseCase, getRatedTvShowsUseCase, getRatedTvShowEpisodesUseCase)
		advanceUntilIdle()

		val expectedState = RatingsUiState.Success(
			ratedMovies = movies.toImmutableList(),
			ratedTvShows = tvShows.toImmutableList(),
			ratedTvEpisodes = tvEpisodes.toImmutableList()
		)
		viewModel.uiState.value shouldBe expectedState
	}

	@Test
	fun `when getRatedMoviesUseCase fails then error state is emitted`() = runTest {
		val error = Failure.UnknownHostFailure
		coEvery { getRatedMoviesUseCase.execute() } returns Err(error)
		coEvery { getRatedTvShowsUseCase.execute() } returns Ok(emptyList())
		coEvery { getRatedTvShowEpisodesUseCase.execute() } returns Ok(emptyList())

		viewModel = RatingsViewModel(getRatedMoviesUseCase, getRatedTvShowsUseCase, getRatedTvShowEpisodesUseCase)
		advanceUntilIdle()

		val expectedState = RatingsUiState.Error(error.message)
		viewModel.uiState.value shouldBe expectedState
	}

}
