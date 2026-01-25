package com.zsoltbertalan.flickslate

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.account.domain.api.AccountRepository
import com.zsoltbertalan.flickslate.account.domain.model.AccountMother
import com.zsoltbertalan.flickslate.base.kotlin.result.Failure
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.shared.domain.model.Account
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeAccountRepository @Inject constructor() : AccountRepository {

	var isLoggedIn = true

	override suspend fun getAccount(): Account = AccountMother.createAccount()

	override suspend fun login(username: String, password: String): Outcome<Account> = Ok(AccountMother.createAccount())

	override suspend fun logout() {
		// no-op
	}

	override suspend fun getAccessToken(): Outcome<String> {
		return if (isLoggedIn) {
			Ok("sessionId")
		} else {
			Err(Failure.UserNotLoggedIn)
		}
	}

}
