package com.zsoltbertalan.flickslate.movies.domain.usecase

import com.github.michaelbull.result.andThen
import com.zsoltbertalan.flickslate.account.domain.usecase.GetSessionIdUseCase
import com.zsoltbertalan.flickslate.movies.domain.api.MovieRatingsRepository
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import javax.inject.Inject

class RateMovieUseCase @Inject constructor(
    private val getSessionIdUseCase: GetSessionIdUseCase,
    private val movieRatingsRepository: MovieRatingsRepository,
) {

    suspend fun execute(movieId: Int, rating: Float): Outcome<Unit> {
        return getSessionIdUseCase.execute()
            .andThen { sessionId ->
                movieRatingsRepository.rateMovie(movieId, rating, sessionId)
            }
    }

}
