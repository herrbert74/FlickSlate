package com.zsoltbertalan.flickslate.account.domain.api

import com.zsoltbertalan.flickslate.shared.domain.model.Account
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome

interface AccountRepository {

	suspend fun getAccount(): Account?
	suspend fun login(username: String, password: String): Outcome<Account>
	suspend fun logout()

}
