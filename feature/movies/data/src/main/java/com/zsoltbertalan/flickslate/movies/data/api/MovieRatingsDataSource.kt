package com.zsoltbertalan.flickslate.movies.data.api

import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome

internal interface MovieRatingsDataSource {

	interface Remote {

		suspend fun rateMovie(movieId: Int, rating: Float): Outcome<Unit>

		suspend fun deleteMovieRating(movieId: Int): Outcome<Unit>

	}

}
