package com.zsoltbertalan.flickslate.account.domain.usecase

import com.github.michaelbull.result.toResultOr
import com.zsoltbertalan.flickslate.account.domain.api.AccountRepository
import com.zsoltbertalan.flickslate.shared.kotlin.result.Failure
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import javax.inject.Inject

class GetAccountIdUseCase @Inject constructor(private val accountRepository: AccountRepository) {

	suspend fun execute(): Outcome<Int> {
		return accountRepository.getAccount()!!.id.toResultOr { Failure.UserNotLoggedIn }
	}

}
