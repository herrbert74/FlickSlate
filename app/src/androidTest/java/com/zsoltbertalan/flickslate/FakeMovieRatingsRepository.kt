package com.zsoltbertalan.flickslate

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.movies.domain.api.MovieRatingsRepository
import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetail
import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetailMother
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeMovieRatingsRepository @Inject constructor() : MovieRatingsRepository {

	var movieDetail: MovieDetail = MovieDetailMother.createMovieDetail()

	override suspend fun rateMovie(movieId: Int, rating: Float, sessionId: String): Outcome<Unit> {
		movieDetail = movieDetail.copy(personalRating = rating)
		AccountListTestState.setMovieRating(movieId, rating)
		return Ok(Unit)
	}

	override suspend fun deleteMovieRating(movieId: Int, sessionId: String): Outcome<Unit> {
		movieDetail = movieDetail.copy(personalRating = -1f)
		AccountListTestState.setMovieRating(movieId, -1f)
		return Ok(Unit)
	}

}
