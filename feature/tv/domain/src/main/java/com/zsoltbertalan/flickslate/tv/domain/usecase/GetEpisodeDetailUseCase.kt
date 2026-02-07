package com.zsoltbertalan.flickslate.tv.domain.usecase

import com.github.michaelbull.result.andThen
import com.zsoltbertalan.flickslate.account.domain.usecase.GetSessionIdUseCase
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.shared.domain.model.TvEpisodeDetail
import com.zsoltbertalan.flickslate.tv.domain.api.TvRepository
import dev.zacsweers.metro.Inject

class GetEpisodeDetailUseCase @Inject constructor(
	private val tvRepository: TvRepository,
	private val getSessionIdUseCase: GetSessionIdUseCase,
) {

	suspend fun execute(seriesId: Int, seasonNumber: Int, episodeNumber: Int): Outcome<TvEpisodeDetail> {
		return getSessionIdUseCase.execute().andThen { sessionId ->
			tvRepository.getTvEpisodeDetail(seriesId, seasonNumber, episodeNumber, sessionId)
		}
	}
}
