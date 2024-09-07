package com.zsoltbertalan.flickslate.search.ui.main

sealed interface SearchEvent {
	data class SearchQuery(val query: String) : SearchEvent
	data object SearchClear : SearchEvent
}
