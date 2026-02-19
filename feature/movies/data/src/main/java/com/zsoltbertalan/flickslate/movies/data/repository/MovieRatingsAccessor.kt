package com.zsoltbertalan.flickslate.movies.data.repository

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.movies.data.api.MovieRatingsDataSource
import com.zsoltbertalan.flickslate.movies.domain.api.MovieRatingsRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
@Inject
class MovieRatingsAccessor internal constructor(
	private val movieRatingsRemoteDataSource: MovieRatingsDataSource.Remote,
) : MovieRatingsRepository {

	override suspend fun rateMovie(movieId: Int, rating: Float, sessionId: String): Outcome<Unit> {
		return movieRatingsRemoteDataSource.rateMovie(movieId, rating, sessionId)
	}

	override suspend fun deleteMovieRating(movieId: Int, sessionId: String): Outcome<Unit> {
		return movieRatingsRemoteDataSource.deleteMovieRating(movieId, sessionId)
	}

}
