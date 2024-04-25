package com.zsoltbertalan.flickslate.ui.search

import com.zsoltbertalan.flickslate.domain.model.Genre
import com.zsoltbertalan.flickslate.domain.model.Movie

data class SearchState(
	val genreResult: List<Genre> = emptyList(),
	val searchQuery: String = "",
	val searchResult: List<Movie> = emptyList(),
)
