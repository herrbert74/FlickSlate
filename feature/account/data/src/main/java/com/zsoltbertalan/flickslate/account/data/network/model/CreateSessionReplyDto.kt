package com.zsoltbertalan.flickslate.account.data.network.model

import kotlinx.serialization.Serializable

@Suppress("ConstructorParameterNaming", "PropertyName")
@Serializable
internal data class CreateSessionReplyDto(val success: Boolean, val session_id: String)
