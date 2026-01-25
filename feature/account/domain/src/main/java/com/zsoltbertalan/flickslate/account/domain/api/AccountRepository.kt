package com.zsoltbertalan.flickslate.account.domain.api

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.shared.domain.model.Account

interface AccountRepository {

	suspend fun getAccount(): Account?
	suspend fun login(username: String, password: String): Outcome<Account>
	suspend fun logout()
	suspend fun getAccessToken(): Outcome<String>

}
