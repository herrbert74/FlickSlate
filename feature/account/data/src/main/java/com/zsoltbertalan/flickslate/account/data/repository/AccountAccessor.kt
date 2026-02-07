package com.zsoltbertalan.flickslate.account.data.repository

import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.onSuccess
import com.github.michaelbull.result.toResultOr
import com.zsoltbertalan.flickslate.account.data.api.AccountDataSource
import com.zsoltbertalan.flickslate.account.domain.api.AccountRepository
import com.zsoltbertalan.flickslate.base.kotlin.result.Failure
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import com.zsoltbertalan.flickslate.shared.domain.model.Account
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import javax.inject.Inject

@ContributesBinding(ActivityRetainedScope::class)
@SingleIn(ActivityRetainedScope::class)
class AccountAccessor @Inject internal constructor(
	private val accountRemoteDataSource: AccountDataSource.Remote,
	private val accountLocalDataSource: AccountDataSource.Local
) : AccountRepository {

	override suspend fun getAccount(): Account? {
		return accountLocalDataSource.getAccount()
	}

	override suspend fun login(username: String, password: String): Outcome<Account> {
		return accountRemoteDataSource.createSessionId(username, password)
			.andThen { sessionId ->
				accountLocalDataSource.saveAccessToken(sessionId)
				accountRemoteDataSource.getAccountDetails(sessionId)
			}.onSuccess { account -> accountLocalDataSource.saveAccount(account) }
	}

	override suspend fun logout() {
		val accessToken = accountLocalDataSource.getAccessToken()
		if (accessToken != null) {
			val success = accountRemoteDataSource.deleteSessionId(accessToken)
			if (success.isOk) accountLocalDataSource.getAccessToken()
		}
	}

	override suspend fun getAccessToken(): Outcome<String> {
		return accountLocalDataSource.getAccessToken().toResultOr { Failure.UserNotLoggedIn }
	}
}
