package com.zsoltbertalan.flickslate.account.domain.model

import com.zsoltbertalan.flickslate.shared.domain.model.Movie

data class RatedMovie(
	val movie: Movie,
	val rating: Float
)
