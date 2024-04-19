package com.zsoltbertalan.flickslate.ui.search

sealed interface SearchEvent {
	data class OpenGenre(val id: Int) : SearchEvent
	data class SearchQuery(val query: String) : SearchEvent
	data object SearchClear : SearchEvent
}
