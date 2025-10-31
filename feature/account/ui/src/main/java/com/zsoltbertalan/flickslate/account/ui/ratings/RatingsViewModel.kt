package com.zsoltbertalan.flickslate.account.ui.ratings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zsoltbertalan.flickslate.account.domain.model.RatedMovie
import com.zsoltbertalan.flickslate.account.domain.model.RatedTvEpisode
import com.zsoltbertalan.flickslate.account.domain.model.RatedTvShow
import com.zsoltbertalan.flickslate.account.domain.usecase.GetRatedMoviesUseCase
import com.zsoltbertalan.flickslate.account.domain.usecase.GetRatedTvShowEpisodesUseCase
import com.zsoltbertalan.flickslate.account.domain.usecase.GetRatedTvShowsUseCase
import com.zsoltbertalan.flickslate.shared.ui.compose.component.paging.PaginationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RatingsViewModel @Inject constructor(
	private val getRatedMoviesUseCase: GetRatedMoviesUseCase,
	private val getRatedTvShowsUseCase: GetRatedTvShowsUseCase,
	private val getRatedTvShowEpisodesUseCase: GetRatedTvShowEpisodesUseCase,
) : ViewModel() {

	val ratedMoviesPaginationState = PaginationState<Int, RatedMovie>(
		initialPageKey = 1,
		onRequestPage = {
			loadRatedMoviesPage(it)
		}
	)

	private fun loadRatedMoviesPage(pageKey: Int) {
		viewModelScope.launch {
			val reply = getRatedMoviesUseCase.execute(pageKey)
			when {
				reply.isOk -> ratedMoviesPaginationState.appendPage(
					reply.value.pagingList,
					if (reply.value.isLastPage) -1 else pageKey + 1,
					isLastPage = reply.value.isLastPage
				)

				else -> ratedMoviesPaginationState.setError(Exception(reply.error.message))
			}
		}
	}

	val ratedTvShowsPaginationState = PaginationState<Int, RatedTvShow>(
		initialPageKey = 1,
		onRequestPage = {
			loadRatedTvShowsPage(it)
		}
	)

	private fun loadRatedTvShowsPage(pageKey: Int) {
		viewModelScope.launch {
			val reply = getRatedTvShowsUseCase.execute(pageKey)
			when {
				reply.isOk -> ratedTvShowsPaginationState.appendPage(
					reply.value.pagingList,
					if (reply.value.isLastPage) -1 else pageKey + 1,
					isLastPage = reply.value.isLastPage
				)

				else -> ratedTvShowsPaginationState.setError(Exception(reply.error.message))
			}
		}
	}

	val ratedTvEpisodesPaginationState = PaginationState<Int, RatedTvEpisode>(
		initialPageKey = 1,
		onRequestPage = {
			loadRatedTvEpisodesPage(it)
		}
	)

	private fun loadRatedTvEpisodesPage(pageKey: Int) {
		viewModelScope.launch {
			val reply = getRatedTvShowEpisodesUseCase.execute(pageKey)
			when {
				reply.isOk -> ratedTvEpisodesPaginationState.appendPage(
					reply.value.pagingList,
					if (reply.value.isLastPage) -1 else pageKey + 1,
					isLastPage = reply.value.isLastPage
				)

				else -> ratedTvEpisodesPaginationState.setError(Exception(reply.error.message))
			}
		}
	}

}
