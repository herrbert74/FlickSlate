package com.zsoltbertalan.flickslate.presentation.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zsoltbertalan.flickslate.domain.api.GenreRepository
import com.zsoltbertalan.flickslate.domain.model.Failure
import com.zsoltbertalan.flickslate.domain.model.Movie
import com.zsoltbertalan.flickslate.presentation.component.paging.PaginationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreDetailViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val genreRepository: GenreRepository,
) : ViewModel() {

	private val genreId: Int = checkNotNull(savedStateHandle["genreId"])
	val genreName: String = checkNotNull(savedStateHandle["genreName"])

	val genreMoviesPaginationState = PaginationState<Int, Movie>(
		initialPageKey = 1,
		onRequestPage = {
			loadGenreMoviesPage(it)
		}
	)

	private fun loadGenreMoviesPage(pageKey: Int) {

		viewModelScope.launch {
			genreRepository.getGenreDetail(genreId = genreId, page = pageKey).collect {
				when {
					it.isOk -> genreMoviesPaginationState.appendPage(
						items = it.value.pagingList,
						nextPageKey = if (it.value.isLastPage) -1 else pageKey + 1,
						isLastPage = it.value.isLastPage
					)

					else -> {
						val e = it.error as? Failure.ServerError
						genreMoviesPaginationState.setError(Exception(e?.message))
					}

				}
			}
		}
	}
}
