package com.zsoltbertalan.flickslate.account.domain.usecase

import com.zsoltbertalan.flickslate.account.domain.api.AccountRepository
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import dev.zacsweers.metro.Inject

class GetSessionIdUseCase @Inject constructor(private val accountRepository: AccountRepository) {

	suspend fun execute(): Outcome<String> {
		return accountRepository.getAccessToken()
	}

}
