package com.zsoltbertalan.flickslate.shared.domain.model

data class TvEpisodeDetail(
	val id: Int,
	val airDate: String,
	val episodeNumber: Int,
	val name: String? = null,
	val overview: String,
	val seasonNumber: Int,
	val stillPath: String?,
	val voteAverage: Float,
	val voteCount: Int,
)
