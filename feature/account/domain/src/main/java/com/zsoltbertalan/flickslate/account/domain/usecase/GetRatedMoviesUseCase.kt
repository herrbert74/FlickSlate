package com.zsoltbertalan.flickslate.account.domain.usecase

import com.github.michaelbull.result.coroutines.coroutineBinding
import com.zsoltbertalan.flickslate.account.domain.api.RatingsRepository
import com.zsoltbertalan.flickslate.account.domain.model.RatedMovie
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import kotlinx.coroutines.async
import javax.inject.Inject

class GetRatedMoviesUseCase @Inject constructor(
	private val ratingsRepository: RatingsRepository,
	private val getAccountIdUseCase: GetAccountIdUseCase,
	private val getSessionIdUseCase: GetSessionIdUseCase,
) {

	suspend fun execute(page: Int): Outcome<PagingReply<RatedMovie>> {
		return coroutineBinding {
			val accountId = async { getAccountIdUseCase.execute().bind() }
			val sessionId = async { getSessionIdUseCase.execute().bind() }
			ratingsRepository.getRatedMovies(accountId.await(), sessionId.await(), page).bind()
		}
	}

}
