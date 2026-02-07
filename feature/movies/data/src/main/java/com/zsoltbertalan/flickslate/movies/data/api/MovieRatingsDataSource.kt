package com.zsoltbertalan.flickslate.movies.data.api

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome

interface MovieRatingsDataSource {

	interface Remote {

		suspend fun rateMovie(movieId: Int, rating: Float, sessionId: String): Outcome<Unit>

		suspend fun deleteMovieRating(movieId: Int, sessionId: String): Outcome<Unit>

	}

}
