package com.zsoltbertalan.flickslate.movies.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zsoltbertalan.flickslate.movies.domain.api.MoviesRepository
import com.zsoltbertalan.flickslate.shared.domain.model.Failure
import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.compose.component.paging.PaginationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val moviesRepository: MoviesRepository) : ViewModel() {

	val popularMoviesPaginationState = PaginationState<Int, Movie>(
		initialPageKey = 1,
		onRequestPage = {
			loadPopularMoviesPage(it)
		}
	)

	private fun loadPopularMoviesPage(pageKey: Int) {

		viewModelScope.launch {
			moviesRepository.getPopularMovies(page = pageKey).collect {
				when {
					it.isOk -> popularMoviesPaginationState.appendPage(
						items = it.value.pagingList,
						nextPageKey = if (it.value.isLastPage) -1 else pageKey + 1,
						isLastPage = it.value.isLastPage
					)

					else -> {
						val e = it.error as? Failure.ServerError
						popularMoviesPaginationState.setError(Exception(e?.message))
					}

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
			moviesRepository.getUpcomingMovies(page = pageKey).collect {
				when {
					it.isOk -> upcomingMoviesPaginationState.appendPage(
						items = it.value.pagingList,
						nextPageKey = if (it.value.isLastPage) -1 else pageKey + 1,
						isLastPage = it.value.isLastPage
					)

					else -> {
						val e = it.error as? Failure.ServerError
						upcomingMoviesPaginationState.setError(Exception(e?.message))
					}

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
			moviesRepository.getNowPlayingMovies(page = pageKey).collect {
				when {
					it.isOk -> nowPlayingMoviesPaginationState.appendPage(
						items = it.value.pagingList,
						nextPageKey = if (it.value.isLastPage) -1 else pageKey + 1,
						isLastPage = it.value.isLastPage
					)

					else -> {
						val e = it.error as? Failure.ServerError
						nowPlayingMoviesPaginationState.setError(Exception(e?.message))
					}

				}
			}
		}
	}

}
