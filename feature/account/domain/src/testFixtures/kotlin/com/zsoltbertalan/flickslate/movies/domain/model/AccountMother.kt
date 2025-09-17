package com.zsoltbertalan.flickslate.movies.domain.model

import com.zsoltbertalan.flickslate.shared.domain.model.Account

object AccountMother {

	fun createAccount() = Account(
		displayName = "John Doe",
		username = "john.doe",
		id = 1234,
		language = "en-GB",
		includeAdult = false
	)

}
