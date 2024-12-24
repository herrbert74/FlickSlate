package com.zsoltbertalan.flickslate.account.data.repository

import com.github.michaelbull.result.andThen
import com.zsoltbertalan.flickslate.account.data.api.AccountDataSource
import com.zsoltbertalan.flickslate.account.domain.api.AccountRepository
import com.zsoltbertalan.flickslate.shared.model.Account
import com.zsoltbertalan.flickslate.shared.util.Outcome
import dagger.hilt.android.scopes.ActivityRetainedScoped
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ActivityRetainedScoped
class AccountAccessor @Inject constructor(
	private val accountRemoteDataSource: AccountDataSource.Remote,
	private val accountLocalDataSource: AccountDataSource.Local
) : AccountRepository {

	override suspend fun getAccount(): Account? {
		return accountLocalDataSource.getAccount()
	}

	override suspend fun login(username: String, password: String): Outcome<Account> {
		return accountRemoteDataSource.createSessionId(username, password)
			.andThen {
				val account = accountRemoteDataSource.getAccountDetails(it)
				accountLocalDataSource.saveAccount(account.value)
				account
			}
	}

	override fun logout() {
		// Do nothing
	}
}
