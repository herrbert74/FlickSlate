package com.zsoltbertalan.flickslate.account.ui.favorites

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.account.domain.model.FavoriteMovie
import com.zsoltbertalan.flickslate.account.domain.model.FavoriteTvShow
import com.zsoltbertalan.flickslate.account.domain.usecase.GetFavoriteMoviesUseCase
import com.zsoltbertalan.flickslate.account.domain.usecase.GetFavoriteTvShowsUseCase
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import com.zsoltbertalan.flickslate.shared.kotlin.result.Failure
import com.zsoltbertalan.flickslate.shared.ui.compose.component.paging.PaginationInternalState
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
class FavoritesViewModelTest {

	private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase = mockk()
	private val getFavoriteTvShowsUseCase: GetFavoriteTvShowsUseCase = mockk()

	private lateinit var viewModel: FavoritesViewModel
	private val dispatcher = StandardTestDispatcher()

	val movies = listOf(FavoriteMovie(Movie(id = 1, title = "Movie 1")))
	val tvShows = listOf(FavoriteTvShow(TvShow(id = 1, name = "Show 1")))

	@Before
	fun setUp() {
		Dispatchers.setMain(dispatcher)

		val moviesPagingReply = PagingReply(movies, isLastPage = false, PageData())
		val tvShowsPagingReply = PagingReply(tvShows, isLastPage = true, PageData())

		coEvery { getFavoriteMoviesUseCase.execute(1) } returns Ok(moviesPagingReply)
		coEvery { getFavoriteTvShowsUseCase.execute(1) } returns Ok(tvShowsPagingReply)
	}

	@After
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun `when viewmodel is created and use cases are successful then loaded state is emitted`() = runTest {
		viewModel = FavoritesViewModel(getFavoriteMoviesUseCase, getFavoriteTvShowsUseCase)

		viewModel.favoriteMoviesPaginationState.onRequestPage(viewModel.favoriteMoviesPaginationState, 1)
		viewModel.favoriteTvShowsPaginationState.onRequestPage(viewModel.favoriteTvShowsPaginationState, 1)

		advanceUntilIdle()

		val moviesState = viewModel.favoriteMoviesPaginationState.internalState.value
		moviesState.shouldBeInstanceOf<PaginationInternalState.Loaded<Int, FavoriteMovie>>()
		moviesState.items shouldBe movies
		moviesState.nextPageKey shouldBe 2

		val tvShowsState = viewModel.favoriteTvShowsPaginationState.internalState.value
		tvShowsState.shouldBeInstanceOf<PaginationInternalState.Loaded<Int, FavoriteTvShow>>()
		tvShowsState.items shouldBe tvShows
		tvShowsState.nextPageKey shouldBe -1
	}

	@Test
	fun `when getFavoriteMoviesUseCase fails then error state is emitted for movies`() = runTest {
		val error = Failure.UnknownHostFailure

		coEvery { getFavoriteMoviesUseCase.execute(1) } returns Err(error)

		viewModel = FavoritesViewModel(getFavoriteMoviesUseCase, getFavoriteTvShowsUseCase)

		viewModel.favoriteMoviesPaginationState.onRequestPage(viewModel.favoriteMoviesPaginationState, 1)
		viewModel.favoriteTvShowsPaginationState.onRequestPage(viewModel.favoriteTvShowsPaginationState, 1)

		advanceUntilIdle()

		val moviesState = viewModel.favoriteMoviesPaginationState.internalState.value
		moviesState.shouldBeInstanceOf<PaginationInternalState.Error<Int, FavoriteMovie>>()
		moviesState.exception.message shouldBe error.message

		val tvShowsState = viewModel.favoriteTvShowsPaginationState.internalState.value
		tvShowsState.shouldBeInstanceOf<PaginationInternalState.Loaded<Int, FavoriteTvShow>>()
		tvShowsState.items shouldBe tvShows
	}

	@Test
	fun `when getFavoriteTvShowsUseCase fails then error state is emitted for tv shows`() = runTest {
		val error = Failure.UnknownHostFailure

		coEvery { getFavoriteTvShowsUseCase.execute(1) } returns Err(error)

		viewModel = FavoritesViewModel(getFavoriteMoviesUseCase, getFavoriteTvShowsUseCase)

		viewModel.favoriteMoviesPaginationState.onRequestPage(viewModel.favoriteMoviesPaginationState, 1)
		viewModel.favoriteTvShowsPaginationState.onRequestPage(viewModel.favoriteTvShowsPaginationState, 1)

		advanceUntilIdle()

		val moviesState = viewModel.favoriteMoviesPaginationState.internalState.value
		moviesState.shouldBeInstanceOf<PaginationInternalState.Loaded<Int, FavoriteMovie>>()
		moviesState.items shouldBe movies

		val tvShowsState = viewModel.favoriteTvShowsPaginationState.internalState.value
		tvShowsState.shouldBeInstanceOf<PaginationInternalState.Error<Int, FavoriteTvShow>>()
		tvShowsState.exception.message shouldBe error.message
	}

	@Test
	fun `when refresh is called then lists are reloaded`() = runTest {
		viewModel = FavoritesViewModel(getFavoriteMoviesUseCase, getFavoriteTvShowsUseCase)

		viewModel.favoriteMoviesPaginationState.onRequestPage(viewModel.favoriteMoviesPaginationState, 1)
		viewModel.favoriteTvShowsPaginationState.onRequestPage(viewModel.favoriteTvShowsPaginationState, 1)
		advanceUntilIdle()

		// Mock new data for refresh
		val newMovies = listOf(FavoriteMovie(Movie(id = 2, title = "Movie 2")))
		val newTvShows = listOf(FavoriteTvShow(TvShow(id = 2, name = "Show 2")))
		coEvery { getFavoriteMoviesUseCase.execute(1) } returns
			Ok(PagingReply(newMovies, isLastPage = true, PageData()))
		coEvery { getFavoriteTvShowsUseCase.execute(1) } returns
			Ok(PagingReply(newTvShows, isLastPage = true, PageData()))

		viewModel.refresh()
		viewModel.favoriteMoviesPaginationState.onRequestPage(viewModel.favoriteMoviesPaginationState, 1)
		viewModel.favoriteTvShowsPaginationState.onRequestPage(viewModel.favoriteTvShowsPaginationState, 1)
		advanceUntilIdle()

		val moviesState = viewModel.favoriteMoviesPaginationState.internalState.value
		moviesState.shouldBeInstanceOf<PaginationInternalState.Loaded<Int, FavoriteMovie>>()
		moviesState.items shouldBe newMovies

		val tvShowsState = viewModel.favoriteTvShowsPaginationState.internalState.value
		tvShowsState.shouldBeInstanceOf<PaginationInternalState.Loaded<Int, FavoriteTvShow>>()
		tvShowsState.items shouldBe newTvShows
	}
}
