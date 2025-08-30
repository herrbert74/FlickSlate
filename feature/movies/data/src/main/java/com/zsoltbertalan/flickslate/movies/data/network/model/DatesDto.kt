package com.zsoltbertalan.flickslate.movies.data.network.model

import kotlinx.serialization.Serializable

@Serializable
internal data class DatesDto(
	val maximum: String,
	val minimum: String
)
