package com.zsoltbertalan.flickslate

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.account.domain.api.AccountRepository
import com.zsoltbertalan.flickslate.movies.domain.model.AccountMother
import com.zsoltbertalan.flickslate.shared.domain.model.Account
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class FakeAccountRepository @Inject constructor() : AccountRepository {

    override suspend fun getAccount(): Account = AccountMother.createAccount()

    override suspend fun login(username: String, password: String): Outcome<Account> = Ok(AccountMother.createAccount())

    override suspend fun logout() {
		// no-op
	}

    override suspend fun getAccessToken(): Outcome<String> = Ok("sessionId")

}
