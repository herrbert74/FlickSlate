package com.zsoltbertalan.flickslate.account.domain.model

import com.zsoltbertalan.flickslate.shared.domain.model.Account

object AccountMother {

	fun createAccount() = Account(
		displayName = "John Doe",
		username = "john.doe",
		id = 1234,
		language = "en-GB",
		region = "GB",
		includeAdult = false
	)

}
