package com.zsoltbertalan.flickslate.movies.domain.usecase

import com.github.michaelbull.result.andThen
import com.zsoltbertalan.flickslate.account.domain.usecase.GetSessionIdUseCase
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.movies.domain.api.MovieRatingsRepository
import javax.inject.Inject

class ChangeMovieRatingUseCase @Inject constructor(
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
