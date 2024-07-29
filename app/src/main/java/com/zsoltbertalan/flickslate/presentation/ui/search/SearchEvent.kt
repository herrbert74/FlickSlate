package com.zsoltbertalan.flickslate.presentation.ui.search

sealed interface SearchEvent {
	data class OpenGenre(val id: Int) : SearchEvent
	data class SearchQuery(val query: String) : SearchEvent
	data object SearchClear : SearchEvent
}
