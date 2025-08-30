package com.zsoltbertalan.flickslate.account.data.api

import com.zsoltbertalan.flickslate.shared.domain.model.Account
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome

internal interface AccountDataSource {

	interface Local {

		fun saveAccessToken(accessToken: String)
		fun getAccessToken(): String?
		fun deleteAccessToken()

		fun saveAccount(account: Account)
		fun getAccount(): Account?

	}

	interface Remote {

		suspend fun createSessionId(username: String, password: String): Outcome<String>

		suspend fun deleteSessionId(sessionId: String): Outcome<Boolean>

		suspend fun getAccountDetails(sessionToken: String): Outcome<Account>

	}

}
