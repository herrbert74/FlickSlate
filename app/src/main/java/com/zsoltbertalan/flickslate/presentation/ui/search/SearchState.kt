package com.zsoltbertalan.flickslate.presentation.ui.search

import com.zsoltbertalan.flickslate.domain.model.Genre
import com.zsoltbertalan.flickslate.domain.model.Movie

data class SearchState(
	val genreResult: List<Genre> = emptyList(),
	val searchQuery: String = "",
	val searchResult: List<Movie> = emptyList(),
)
