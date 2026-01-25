package com.zsoltbertalan.flickslate.movies.domain.api

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome

interface MovieRatingsRepository {

	suspend fun rateMovie(movieId: Int, rating: Float, sessionId: String): Outcome<Unit>

	suspend fun deleteMovieRating(movieId: Int, sessionId: String): Outcome<Unit>

}
