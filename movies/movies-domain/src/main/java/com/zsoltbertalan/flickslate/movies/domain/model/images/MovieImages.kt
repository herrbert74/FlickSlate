package com.zsoltbertalan.flickslate.movies.domain.model.images

data class MovieImages(
	val backdrops: List<Image> = emptyList(),
	val logos: List<Image> = emptyList(),
	val posters: List<Image> = emptyList(),
	val id: Int = 0
)
