package com.zsoltbertalan.flickslate.search.ui.main

import com.zsoltbertalan.flickslate.shared.domain.model.Genre
import com.zsoltbertalan.flickslate.movies.domain.model.Movie

data class SearchState(
	val genreResult: List<Genre> = emptyList(),
	val searchQuery: String = "",
	val searchResult: List<Movie> = emptyList(),
)
