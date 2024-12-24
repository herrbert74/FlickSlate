package com.zsoltbertalan.flickslate.account.domain.api

import com.zsoltbertalan.flickslate.shared.model.Account
import com.zsoltbertalan.flickslate.shared.util.Outcome

interface AccountRepository {

	suspend fun getAccount(): Account?
	suspend fun login(username: String, password: String): Outcome<Account>
	fun logout()

}
