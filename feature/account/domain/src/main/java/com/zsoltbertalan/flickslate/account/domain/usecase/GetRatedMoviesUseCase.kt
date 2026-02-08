package com.zsoltbertalan.flickslate.account.domain.usecase

import com.github.michaelbull.result.coroutines.coroutineBinding
import com.zsoltbertalan.flickslate.account.domain.api.RatingsRepository
import com.zsoltbertalan.flickslate.account.domain.model.RatedMovie
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.async

class GetRatedMoviesUseCase @Inject internal constructor(
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
