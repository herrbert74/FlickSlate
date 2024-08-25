package com.zsoltbertalan.flickslate.movies.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class DatesDto(
	val maximum: String,
	val minimum: String
)
