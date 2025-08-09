package com.zsoltbertalan.flickslate.account.data.network.model

import kotlinx.serialization.Serializable

@Suppress("ConstructorParameterNaming", "PropertyName")
@Serializable
data class CreateSessionReplyDto(val success: Boolean, val session_id: String)
