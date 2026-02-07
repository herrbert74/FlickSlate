package com.zsoltbertalan.flickslate.movies.data.repository

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.movies.data.api.MovieRatingsDataSource
import com.zsoltbertalan.flickslate.movies.domain.api.MovieRatingsRepository
import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import javax.inject.Inject

@ContributesBinding(ActivityRetainedScope::class)
@SingleIn(ActivityRetainedScope::class)
internal class MovieRatingsAccessor @Inject constructor(
	private val movieRatingsRemoteDataSource: MovieRatingsDataSource.Remote,
) : MovieRatingsRepository {

	override suspend fun rateMovie(movieId: Int, rating: Float, sessionId: String): Outcome<Unit> {
		return movieRatingsRemoteDataSource.rateMovie(movieId, rating, sessionId)
	}

	override suspend fun deleteMovieRating(movieId: Int, sessionId: String): Outcome<Unit> {
		return movieRatingsRemoteDataSource.deleteMovieRating(movieId, sessionId)
	}

}
