package com.zsoltbertalan.flickslate.account.ui.ratings

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.account.domain.model.RatedMovie
import com.zsoltbertalan.flickslate.account.domain.model.RatedMovieMother
import com.zsoltbertalan.flickslate.account.domain.model.RatedTvEpisode
import com.zsoltbertalan.flickslate.account.domain.model.RatedTvShow
import com.zsoltbertalan.flickslate.account.domain.usecase.GetRatedMoviesUseCase
import com.zsoltbertalan.flickslate.account.domain.usecase.GetRatedTvShowEpisodesUseCase
import com.zsoltbertalan.flickslate.account.domain.usecase.GetRatedTvShowsUseCase
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.base.kotlin.result.Failure
import com.zsoltbertalan.flickslate.shared.ui.compose.component.paging.PaginationInternalState
import com.zsoltbertalan.flickslate.tv.domain.model.TvMother
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
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

@OptIn(ExperimentalCoroutinesApi::class)
class RatingsViewModelTest {

	private val getRatedMoviesUseCase: GetRatedMoviesUseCase = mockk()
	private val getRatedTvShowsUseCase: GetRatedTvShowsUseCase = mockk()
	private val getRatedTvShowEpisodesUseCase: GetRatedTvShowEpisodesUseCase = mockk()

	private lateinit var viewModel: RatingsViewModel
	private val dispatcher = StandardTestDispatcher()

	val movies = RatedMovieMother.createRatedMovieList()
	val tvShows = listOf(RatedTvShow(TvMother.createTvList().first(), 7.0f))
	val tvEpisodes = listOf(RatedTvEpisode(TvMother.createSeasonDetail(1, 1).episodes.first(), 8.0f))

	@Before
	fun setUp() {
		Dispatchers.setMain(dispatcher)

		val moviesPagingReply = PagingReply(movies, isLastPage = false, PageData())
		val tvShowsPagingReply = PagingReply(tvShows, isLastPage = true, PageData())
		val tvEpisodesPagingReply = PagingReply(tvEpisodes, isLastPage = true, PageData())

		coEvery { getRatedMoviesUseCase.execute(1) } returns Ok(moviesPagingReply)
		coEvery { getRatedTvShowsUseCase.execute(1) } returns Ok(tvShowsPagingReply)
		coEvery { getRatedTvShowEpisodesUseCase.execute(1) } returns Ok(tvEpisodesPagingReply)
	}

	@After
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun `when viewmodel is created and use cases are successful then loaded state is emitted`() = runTest {
		viewModel = RatingsViewModel(getRatedMoviesUseCase, getRatedTvShowsUseCase, getRatedTvShowEpisodesUseCase)

		viewModel.ratedMoviesPaginationState.onRequestPage(viewModel.ratedMoviesPaginationState, 1)
		viewModel.ratedTvShowsPaginationState.onRequestPage(viewModel.ratedTvShowsPaginationState, 1)
		viewModel.ratedTvEpisodesPaginationState.onRequestPage(viewModel.ratedTvEpisodesPaginationState, 1)

		advanceUntilIdle()

		val moviesState = viewModel.ratedMoviesPaginationState.internalState.value
		moviesState.shouldBeInstanceOf<PaginationInternalState.Loaded<Int, RatedMovie>>()
		moviesState.items shouldBe movies
		moviesState.nextPageKey shouldBe 2

		val tvShowsState = viewModel.ratedTvShowsPaginationState.internalState.value
		tvShowsState.shouldBeInstanceOf<PaginationInternalState.Loaded<Int, RatedTvShow>>()
		tvShowsState.items shouldBe tvShows
		tvShowsState.nextPageKey shouldBe -1

		val tvEpisodesState = viewModel.ratedTvEpisodesPaginationState.internalState.value
		tvEpisodesState.shouldBeInstanceOf<PaginationInternalState.Loaded<Int, RatedTvEpisode>>()
		tvEpisodesState.items shouldBe tvEpisodes
		tvEpisodesState.nextPageKey shouldBe -1
	}

	@Test
	fun `when getRatedMoviesUseCase fails then error state is emitted for movies`() = runTest {
		val error = Failure.UnknownHostFailure

		coEvery { getRatedMoviesUseCase.execute(1) } returns Err(error)

		viewModel = RatingsViewModel(getRatedMoviesUseCase, getRatedTvShowsUseCase, getRatedTvShowEpisodesUseCase)

		viewModel.ratedMoviesPaginationState.onRequestPage(viewModel.ratedMoviesPaginationState, 1)
		viewModel.ratedTvShowsPaginationState.onRequestPage(viewModel.ratedTvShowsPaginationState, 1)
		viewModel.ratedTvEpisodesPaginationState.onRequestPage(viewModel.ratedTvEpisodesPaginationState, 1)

		advanceUntilIdle()

		val moviesState = viewModel.ratedMoviesPaginationState.internalState.value
		moviesState.shouldBeInstanceOf<PaginationInternalState.Error<Int, RatedMovie>>()
		moviesState.exception.message shouldBe error.message

		val tvShowsState = viewModel.ratedTvShowsPaginationState.internalState.value
		tvShowsState.shouldBeInstanceOf<PaginationInternalState.Loaded<Int, RatedTvShow>>()
		tvShowsState.items shouldBe tvShows

		val tvEpisodesState = viewModel.ratedTvEpisodesPaginationState.internalState.value
		tvEpisodesState.shouldBeInstanceOf<PaginationInternalState.Loaded<Int, RatedTvEpisode>>()
		tvEpisodesState.items shouldBe tvEpisodes
	}

	@Test
	fun `when refresh is called, states are reset to Initial`() = runTest {
		viewModel = RatingsViewModel(getRatedMoviesUseCase, getRatedTvShowsUseCase, getRatedTvShowEpisodesUseCase)

		// Load initial data
		viewModel.ratedMoviesPaginationState.onRequestPage(viewModel.ratedMoviesPaginationState, 1)
		advanceUntilIdle()

		// Verify loaded
		viewModel.ratedMoviesPaginationState.internalState.value
			.shouldBeInstanceOf<PaginationInternalState.Loaded<Int, RatedMovie>>()

		// Call refresh
		viewModel.refresh()

		// Verify reset to Initial
		viewModel.ratedMoviesPaginationState.internalState.value
			.shouldBeInstanceOf<PaginationInternalState.Initial<Int, RatedMovie>>()
		viewModel.ratedTvShowsPaginationState.internalState.value
			.shouldBeInstanceOf<PaginationInternalState.Initial<Int, RatedTvShow>>()
		viewModel.ratedTvEpisodesPaginationState.internalState.value
			.shouldBeInstanceOf<PaginationInternalState.Initial<Int, RatedTvEpisode>>()
	}

}
