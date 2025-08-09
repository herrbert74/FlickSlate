package com.zsoltbertalan.flickslate.movies.domain.model

import com.zsoltbertalan.flickslate.shared.model.Account

object AccountMother {

	fun createAccount() = Account(
		name = "John Doe"
	)

}
