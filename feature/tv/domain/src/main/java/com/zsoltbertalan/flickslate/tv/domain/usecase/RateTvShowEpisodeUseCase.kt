package com.zsoltbertalan.flickslate.tv.domain.usecase

import com.github.michaelbull.result.andThen
import com.zsoltbertalan.flickslate.account.domain.usecase.GetSessionIdUseCase
import com.zsoltbertalan.flickslate.tv.domain.api.TvRatingsRepository
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import javax.inject.Inject

class RateTvShowEpisodeUseCase @Inject constructor(
    private val getSessionIdUseCase: GetSessionIdUseCase,
    private val tvRatingsRepository: TvRatingsRepository,
) {

    suspend fun execute(tvShowId: Int, seasonNumber: Int, episodeNumber: Int, rating: Float): Outcome<Unit> {
        return getSessionIdUseCase.execute()
            .andThen { sessionId ->
                tvRatingsRepository.rateTvShowEpisode(tvShowId, seasonNumber, episodeNumber, rating, sessionId)
            }
    }

}
