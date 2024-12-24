package com.zsoltbertalan.flickslate.account.data.api

import com.zsoltbertalan.flickslate.shared.model.Account
import com.zsoltbertalan.flickslate.shared.util.Outcome

interface AccountDataSource {

	interface Local {

		fun saveAccessToken(accessToken: String)
		fun getAccessToken(): String?

		fun saveAccount(account: Account)
		fun getAccount(): Account?

	}

	interface Remote {

		suspend fun createSessionId(username: String, password: String): Outcome<String>

		suspend fun getAccountDetails(sessionToken: String): Outcome<Account>

	}

}
