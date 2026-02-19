package com.zsoltbertalan.flickslate.account.domain.usecase

import com.github.michaelbull.result.toResultOr
import com.zsoltbertalan.flickslate.account.domain.api.AccountRepository
import com.zsoltbertalan.flickslate.base.kotlin.result.Failure
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import dev.zacsweers.metro.Inject

@Inject
class GetAccountIdUseCase internal constructor(private val accountRepository: AccountRepository) {

	suspend fun execute(): Outcome<Int> {
		return accountRepository.getAccount()!!.id.toResultOr { Failure.UserNotLoggedIn }
	}

}
