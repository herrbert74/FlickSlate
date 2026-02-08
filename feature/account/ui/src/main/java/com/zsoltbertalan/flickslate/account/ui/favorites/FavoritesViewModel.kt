package com.zsoltbertalan.flickslate.account.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.zsoltbertalan.flickslate.account.domain.model.FavoriteMovie
import com.zsoltbertalan.flickslate.account.domain.model.FavoriteTvShow
import com.zsoltbertalan.flickslate.account.domain.usecase.GetFavoriteMoviesUseCase
import com.zsoltbertalan.flickslate.account.domain.usecase.GetFavoriteTvShowsUseCase
import com.zsoltbertalan.flickslate.shared.ui.compose.component.paging.PaginationState
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.launch

@ViewModelKey(FavoritesViewModel::class)
@ContributesIntoMap(AppScope::class)
class FavoritesViewModel @Inject constructor(
	private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
	private val getFavoriteTvShowsUseCase: GetFavoriteTvShowsUseCase,
) : ViewModel() {

	val favoriteMoviesPaginationState = PaginationState<Int, FavoriteMovie>(
		initialPageKey = 1,
		onRequestPage = {
			loadFavoriteMoviesPage(it)
		}
	)

	private fun loadFavoriteMoviesPage(pageKey: Int) {
		viewModelScope.launch {
			val reply = getFavoriteMoviesUseCase.execute(pageKey)
			reply.onSuccess { pagingReply ->
				favoriteMoviesPaginationState.appendPage(
					pageKey = pageKey,
					items = pagingReply.pagingList,
					nextPageKey = if (pagingReply.isLastPage) -1 else pageKey + 1,
					isLastPage = pagingReply.isLastPage
				)
			}.onFailure { failure ->
				favoriteMoviesPaginationState.setError(Exception(failure.message))
			}
		}
	}

	val favoriteTvShowsPaginationState = PaginationState<Int, FavoriteTvShow>(
		initialPageKey = 1,
		onRequestPage = {
			loadFavoriteTvShowsPage(it)
		}
	)

	private fun loadFavoriteTvShowsPage(pageKey: Int) {
		viewModelScope.launch {
			val reply = getFavoriteTvShowsUseCase.execute(pageKey)
			reply.onSuccess { pagingReply ->
				favoriteTvShowsPaginationState.appendPage(
					pageKey = pageKey,
					items = pagingReply.pagingList,
					nextPageKey = if (pagingReply.isLastPage) -1 else pageKey + 1,
					isLastPage = pagingReply.isLastPage
				)
			}.onFailure { failure ->
				favoriteTvShowsPaginationState.setError(Exception(failure.message))
			}
		}
	}

	fun refresh() {
		favoriteMoviesPaginationState.refresh()
		favoriteTvShowsPaginationState.refresh()
	}
}
