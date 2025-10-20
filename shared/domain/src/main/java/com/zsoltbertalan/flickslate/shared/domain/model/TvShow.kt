package com.zsoltbertalan.flickslate.shared.domain.model

data class TvShow(
	val id: Int = 0,
	val name: String = "",
	val overview: String? = null,
	val voteAverage: Float? = null,
	val posterPath: String? = null,
	val backdropPath: String? = null,
)
