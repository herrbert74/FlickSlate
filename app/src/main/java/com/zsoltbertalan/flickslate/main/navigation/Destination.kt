package com.zsoltbertalan.flickslate.main.navigation

import androidx.annotation.StringRes
import com.zsoltbertalan.flickslate.R

enum class Destination(
	@StringRes val titleId: Int,
	val route: String
) {
	MOVIE(R.string.movies, "movies"),
	TV(R.string.tv, "tv"),
	SEARCH(R.string.search, "search"),
}
