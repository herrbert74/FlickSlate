package com.zsoltbertalan.flickslate.tv.domain.model

data class Season(
	val airDate: String,
	val episodeCount: Int,
	val id: Int,
	val name: String?,
	val overview: String?,
	val posterPath: String?,
	val seasonNumber: Int?
)
