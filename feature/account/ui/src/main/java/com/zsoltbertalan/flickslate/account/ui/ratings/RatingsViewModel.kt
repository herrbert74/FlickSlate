package com.zsoltbertalan.flickslate.account.ui.ratings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
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
			reply.onSuccess { pagingReply ->
				ratedMoviesPaginationState.appendPage(
					pageKey = pageKey,
					items = pagingReply.pagingList,
					nextPageKey = if (pagingReply.isLastPage) -1 else pageKey + 1,
					isLastPage = pagingReply.isLastPage
				)
			}.onFailure { failure ->
				ratedMoviesPaginationState.setError(Exception(failure.message))
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
			reply.onSuccess { pagingReply ->
				ratedTvShowsPaginationState.appendPage(
					pageKey = pageKey,
					items = pagingReply.pagingList,
					nextPageKey = if (pagingReply.isLastPage) -1 else pageKey + 1,
					isLastPage = pagingReply.isLastPage
				)
			}.onFailure { failure ->
				ratedTvShowsPaginationState.setError(Exception(failure.message))
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
			reply.onSuccess { pagingReply ->
				ratedTvEpisodesPaginationState.appendPage(
					pageKey = pageKey,
					items = pagingReply.pagingList,
					nextPageKey = if (pagingReply.isLastPage) -1 else pageKey + 1,
					isLastPage = pagingReply.isLastPage
				)
			}.onFailure { failure ->
				ratedTvEpisodesPaginationState.setError(Exception(failure.message))
			}
		}
	}

	fun refresh() {
		ratedMoviesPaginationState.refresh()
		ratedTvShowsPaginationState.refresh()
		ratedTvEpisodesPaginationState.refresh()
	}

}
