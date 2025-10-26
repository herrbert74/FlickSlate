package com.zsoltbertalan.flickslate.account.domain.usecase

import com.github.michaelbull.result.coroutines.coroutineBinding
import com.zsoltbertalan.flickslate.account.domain.api.RatingsRepository
import com.zsoltbertalan.flickslate.account.domain.model.RatedTvEpisode
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import kotlinx.coroutines.async
import javax.inject.Inject

class GetRatedTvShowEpisodesUseCase @Inject constructor(
	private val ratingsRepository: RatingsRepository,
	private val getAccountIdUseCase: GetAccountIdUseCase,
	private val getSessionIdUseCase: GetSessionIdUseCase,
) {

	suspend fun execute(): Outcome<List<RatedTvEpisode>> {
		return coroutineBinding {
			val accountId = async { getAccountIdUseCase.execute().bind() }
			val sessionId = async { getSessionIdUseCase.execute().bind() }
			ratingsRepository.getRatedTvShowEpisodes(accountId.await(), sessionId.await()).bind()
		}
	}

}
