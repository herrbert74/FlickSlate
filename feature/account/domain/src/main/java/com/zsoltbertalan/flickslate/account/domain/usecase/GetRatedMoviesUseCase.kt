package com.zsoltbertalan.flickslate.account.domain.usecase

import com.zsoltbertalan.flickslate.account.domain.api.RatingsRepository
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import javax.inject.Inject

class GetRatedMoviesUseCase @Inject constructor(private val ratingsRepository: RatingsRepository) {

    suspend fun execute(): Outcome<List<Movie>> {
        return ratingsRepository.getRatedMovies()
    }

}
