package com.zsoltbertalan.flickslate.movies.data.network.model

import com.zsoltbertalan.flickslate.account.data.network.model.AccountDetailsReplyDto

object AccountReplyDtoMother {

	fun createAccountReplyDto() = AccountDetailsReplyDto(
		name = "John Doe",
		username = "Jane Doe"
	)

	fun createAccountReplyDtoWithoutName() = AccountDetailsReplyDto(
		name = null,
		username = "Jane Doe"
	)

}
