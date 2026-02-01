package com.zsoltbertalan.flickslate.movies.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.zsoltbertalan.flickslate.account.domain.api.AccountRepository
import com.zsoltbertalan.flickslate.movies.domain.api.MoviesRepository
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.ui.compose.component.paging.PaginationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
	private val moviesRepository: MoviesRepository,
	private val accountRepository: AccountRepository
) : ViewModel() {

	val popularMoviesPaginationState = PaginationState<Int, Movie>(
		initialPageKey = 1,
		onRequestPage = {
			loadPopularMoviesPage(it)
		}
	)

	private fun loadPopularMoviesPage(pageKey: Int) {
		viewModelScope.launch {
			moviesRepository.getPopularMovies(page = pageKey).collect { outcome ->
				outcome.onSuccess { pagingReply ->
					popularMoviesPaginationState.appendPage(
						pageKey = pageKey,
						items = pagingReply.pagingList,
						nextPageKey = if (pagingReply.isLastPage) -1 else pageKey + 1,
						isLastPage = pagingReply.isLastPage
					)
				}

				outcome.onFailure { failure ->
					popularMoviesPaginationState.setError(Exception(failure.message))

				}
			}
		}
	}

	val upcomingMoviesPaginationState = PaginationState<Int, Movie>(
		initialPageKey = 1,
		onRequestPage = {
			loadUpcomingMoviesPage(it)
		}
	)

	private fun loadUpcomingMoviesPage(pageKey: Int) {
		viewModelScope.launch {
			val account = accountRepository.getAccount()
			val region = account?.region ?: "US"
			moviesRepository.getUpcomingMovies(page = pageKey, region = region).collect {
				it.onSuccess { pagingReply ->
					upcomingMoviesPaginationState.appendPage(
						pageKey = pageKey,
						items = pagingReply.pagingList,
						nextPageKey = if (pagingReply.isLastPage) -1 else pageKey + 1,
						isLastPage = pagingReply.isLastPage
					)

				}.onFailure { failure ->
					upcomingMoviesPaginationState.setError(Exception(failure.message))
				}
			}
		}
	}

	val nowPlayingMoviesPaginationState = PaginationState<Int, Movie>(
		initialPageKey = 1,
		onRequestPage = {
			loadNowPlayingMoviesPage(it)
		}
	)

	private fun loadNowPlayingMoviesPage(pageKey: Int) {
		viewModelScope.launch {
			val account = accountRepository.getAccount()
			val region = account?.region ?: "US"
			moviesRepository.getNowPlayingMovies(page = pageKey, region = region).collect {
				it.onSuccess { pagingReply ->
					nowPlayingMoviesPaginationState.appendPage(
						pageKey = pageKey,
						items = pagingReply.pagingList,
						nextPageKey = if (pagingReply.isLastPage) -1 else pageKey + 1,
						isLastPage = pagingReply.isLastPage
					)
				}.onFailure { failure ->
					nowPlayingMoviesPaginationState.setError(Exception(failure.message))
				}
			}
		}
	}

}
