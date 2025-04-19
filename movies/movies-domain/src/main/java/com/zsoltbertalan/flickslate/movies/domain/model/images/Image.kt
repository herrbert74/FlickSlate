package com.zsoltbertalan.flickslate.movies.domain.model.images

data class Image(
	val aspectRatio: Double,
	val filePath: String,
	val height: Int,
	val iso639dash1: String?,
	val voteAverage: Double,
	val voteCount: Int,
	val width: Int
)
