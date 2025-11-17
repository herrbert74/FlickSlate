package com.zsoltbertalan.flickslate

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.movies.domain.api.MovieRatingsRepository
import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetail
import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetailMother
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeMovieRatingsRepository @Inject constructor() : MovieRatingsRepository {

	var movieDetail: MovieDetail = MovieDetailMother.createMovieDetail()

	override suspend fun rateMovie(movieId: Int, rating: Float, sessionId: String): Outcome<Unit> {
		movieDetail = movieDetail.copy(personalRating = rating)
		return Ok(Unit)
	}

	override suspend fun deleteMovieRating(movieId: Int, sessionId: String): Outcome<Unit> {
		movieDetail = movieDetail.copy(personalRating = 0f)
		return Ok(Unit)
	}

}
