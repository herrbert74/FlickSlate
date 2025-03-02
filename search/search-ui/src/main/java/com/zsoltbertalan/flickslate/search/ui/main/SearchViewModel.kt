package com.zsoltbertalan.flickslate.search.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zsoltbertalan.flickslate.search.domain.api.GenreRepository
import com.zsoltbertalan.flickslate.search.domain.api.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
	private val genreRepository: GenreRepository,
	private val searchRepository: SearchRepository
) : ViewModel() {

	private val _searchStateData = MutableStateFlow(SearchState())
	val searchStateData = _searchStateData.asStateFlow()

	private val _searchEvent = MutableSharedFlow<SearchEvent>()
	private val searchEvent = _searchEvent.asSharedFlow()

	suspend fun emitEvent(event: SearchEvent) = _searchEvent.emit(event)

	init {
		searchEvent.onEach {
			listenEvent(it)
		}.launchIn(viewModelScope)
		setupSearchScreen()
	}

	private fun listenEvent(event: SearchEvent) {

		when (event) {
			is SearchEvent.SearchQuery ->
				viewModelScope.launch {
					_searchStateData.update {
						it.copy(searchQuery = event.query)
					}

					if (event.query.isNotEmpty()) {
						val response = searchRepository.getSearchResult(event.query)
						when {
							response.isOk -> _searchStateData.update {
								it.copy(searchResult = response.value.pagingList)
							}

							else -> Unit // handle failure
						}
					} else {
						_searchStateData.update {
							it.copy(
								searchResult = emptyList(),
								searchQuery = ""
							)
						}
					}
				}

			is SearchEvent.SearchClear -> _searchStateData.update {
				it.copy(
					searchResult = emptyList(),
					searchQuery = ""
				)
			}

		}
	}

	private fun setupSearchScreen() {
		viewModelScope.launch {
			genreRepository.getGenresList().collect { response ->
				when {
					response.isOk -> _searchStateData.update {
						it.copy(
							genreResult = response.value.genres.orEmpty().toImmutableList(),
						)
					}

					else -> Unit // handle failure
				}
			}
		}
	}

}
