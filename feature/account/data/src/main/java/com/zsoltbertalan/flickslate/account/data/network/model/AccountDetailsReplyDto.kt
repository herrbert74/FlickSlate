package com.zsoltbertalan.flickslate.account.data.network.model

import com.zsoltbertalan.flickslate.shared.domain.model.Account
import kotlinx.serialization.Serializable

@Suppress("ConstructorParameterNaming")
@Serializable
internal data class AccountDetailsReplyDto(
	val name: String?,
	val username: String,
	val iso_639_1: String,
	val iso_3166_1: String,
	val id: Int,
	val include_adult: Boolean,
)

internal fun AccountDetailsReplyDto.toAccount(): Account {
	val accountName = if (name.isNullOrEmpty()) username else name
	return Account(accountName, username, "${iso_639_1}-${iso_3166_1}", iso_3166_1, id, include_adult)
}
