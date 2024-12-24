package com.zsoltbertalan.flickslate.account.data.network.model

import kotlinx.serialization.Serializable

@Suppress("ConstructorParameterNaming")
@Serializable
data class AccountDetailsReplyDto(val name: String?, val username: String)
