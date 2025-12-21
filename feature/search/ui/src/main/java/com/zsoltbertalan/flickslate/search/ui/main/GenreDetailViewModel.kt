package com.zsoltbertalan.flickslate.search.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zsoltbertalan.flickslate.search.domain.api.GenreRepository
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.kotlin.result.Failure
import com.zsoltbertalan.flickslate.shared.ui.compose.component.paging.PaginationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreDetailViewModel @Inject constructor(
	private val savedStateHandle: SavedStateHandle,
	private val genreRepository: GenreRepository,
) : ViewModel() {

	private val genreId: Int
		get() = savedStateHandle["genreId"] ?: -1
	val genreName: String
		get() = savedStateHandle["genreName"] ?: ""

	val genreMoviesPaginationState = PaginationState<Int, Movie>(
		initialPageKey = 1,
		onRequestPage = {
			loadGenreMoviesPage(it)
		}
	)

	fun load(id: Int, name: String) {
		if (genreId == id && genreName == name) return
		savedStateHandle["genreId"] = id
		savedStateHandle["genreName"] = name
		genreMoviesPaginationState.refresh()
	}

	private fun loadGenreMoviesPage(pageKey: Int) {
		if (genreId == -1) return
		viewModelScope.launch {
			genreRepository.getGenreDetail(genreId = genreId, page = pageKey).collect {
				when {
					it.isOk -> genreMoviesPaginationState.appendPage(
						items = it.value.pagingReply.pagingList,
						nextPageKey = if (it.value.pagingReply.isLastPage) -1 else pageKey + 1,
						isLastPage = it.value.pagingReply.isLastPage
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
