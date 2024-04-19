package com.zsoltbertalan.flickslate.ui.search

import androidx.compose.ui.graphics.Color
import com.zsoltbertalan.flickslate.domain.model.Genre

data class SearchState(
	val genreResult: List<Genre> = emptyList(),
	val searchTextField: String = "",
	val listOfColors: List<Color> = emptyList(),
	val searchResult: List<String> = emptyList(),
)
