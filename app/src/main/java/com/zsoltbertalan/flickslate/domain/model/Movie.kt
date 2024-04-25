package com.zsoltbertalan.flickslate.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
	val id: Int = 0,
	val title: String,
	val overview: String? = null,
	val voteAverage: Float? = null,
	val posterPath: String? = null,
	val backdropPath: String? = null,
)
