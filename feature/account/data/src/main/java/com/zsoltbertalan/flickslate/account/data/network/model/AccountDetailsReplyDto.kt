package com.zsoltbertalan.flickslate.account.data.network.model

import com.zsoltbertalan.flickslate.shared.domain.model.Account
import kotlinx.serialization.Serializable

@Suppress("ConstructorParameterNaming")
@Serializable
internal data class AccountDetailsReplyDto(val name: String?, val username: String)

internal fun AccountDetailsReplyDto.toAccount(): Account {
	val accountName = if (name.isNullOrEmpty()) username else name
	return Account(accountName)
}
