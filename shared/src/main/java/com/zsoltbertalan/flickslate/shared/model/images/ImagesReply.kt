package com.zsoltbertalan.flickslate.shared.model.images

data class ImagesReply(
	val backdrops: List<Image> = emptyList(),
	val logos: List<Image> = emptyList(),
	val posters: List<Image> = emptyList(),
	val id: Int = 0
)
