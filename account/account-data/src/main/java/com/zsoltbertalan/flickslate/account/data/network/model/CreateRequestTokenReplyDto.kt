package com.zsoltbertalan.flickslate.account.data.network.model

import kotlinx.serialization.Serializable

@Suppress("ConstructorParameterNaming", "PropertyName")
@Serializable
data class CreateRequestTokenReplyDto(val success: Boolean, val expires_at: String, val request_token: String)
