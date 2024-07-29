package com.zsoltbertalan.flickslate.presentation.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.zsoltbertalan.flickslate.domain.api.GenreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GenreDetailViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	genreRepository: GenreRepository,
) : ViewModel() {

	private val genreId: Int = checkNotNull(savedStateHandle["genreId"])
	val genreName: String = checkNotNull(savedStateHandle["genreName"])

	val genreDetailList = genreRepository.getGenreDetail(genreId).cachedIn(viewModelScope)
}
