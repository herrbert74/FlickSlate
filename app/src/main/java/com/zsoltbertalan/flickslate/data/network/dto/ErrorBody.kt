package com.zsoltbertalan.flickslate.data.network.dto

import kotlinx.serialization.Serializable

@Suppress("PropertyName")
@Serializable
data class ErrorBody(val success: Boolean, val status_code: Int, val status_message: String)
