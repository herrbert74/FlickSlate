package com.zsoltbertalan.flickslate.account.domain.model

import com.zsoltbertalan.flickslate.shared.domain.model.Movie

/** Domain model representing a movie that is in the user's favorites list. */
data class FavoriteMovie(
	val movie: Movie,
)
