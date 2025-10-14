package com.zsoltbertalan.flickslate.account.data.repository

import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.toResultOr
import com.zsoltbertalan.flickslate.account.data.api.AccountDataSource
import com.zsoltbertalan.flickslate.account.domain.api.AccountRepository
import com.zsoltbertalan.flickslate.shared.domain.model.Account
import com.zsoltbertalan.flickslate.shared.kotlin.result.Failure
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import dagger.hilt.android.scopes.ActivityRetainedScoped
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ActivityRetainedScoped
internal class AccountAccessor @Inject constructor(
	private val accountRemoteDataSource: AccountDataSource.Remote,
	private val accountLocalDataSource: AccountDataSource.Local
) : AccountRepository {

	override suspend fun getAccount(): Account? {
		return accountLocalDataSource.getAccount()
	}

	override suspend fun login(username: String, password: String): Outcome<Account> {
		return accountRemoteDataSource.createSessionId(username, password)
			.andThen {
				accountLocalDataSource.saveAccessToken(it)
				val account = accountRemoteDataSource.getAccountDetails(it)
				accountLocalDataSource.saveAccount(account.value)
				account
			}
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
