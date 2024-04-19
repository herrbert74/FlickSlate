package com.zsoltbertalan.flickslate.domain.model

data class MovieDetail(
	val id: Int? = null,
	val title: String? = null,
	val overview: String? = null,
	val voteAverage: Float? = null,
	val posterPath: String? = null,
	val backdropPath: String? = null,
	val genres: List<Genre> = emptyList()
)
