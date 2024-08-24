package com.zsoltbertalan.flickslate.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class DatesDto(
	val maximum: String,
	val minimum: String
)
