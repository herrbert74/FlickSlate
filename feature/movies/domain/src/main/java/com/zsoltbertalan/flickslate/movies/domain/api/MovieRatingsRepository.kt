package com.zsoltbertalan.flickslate.movies.domain.api

import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome

interface MovieRatingsRepository {

	suspend fun rateMovie(movieId: Int, rating: Float): Outcome<Unit>

	suspend fun deleteMovieRating(movieId: Int): Outcome<Unit>

}
