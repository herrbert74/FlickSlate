package com.zsoltbertalan.flickslate.search.ui.main

import androidx.compose.runtime.Immutable
import com.zsoltbertalan.flickslate.shared.domain.model.Genre
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class SearchState(
	val genreResult: ImmutableList<Genre> = listOf<Genre>().toImmutableList(),
	val searchQuery: String = "",
	val searchResult: List<Movie> = emptyList(),
)
