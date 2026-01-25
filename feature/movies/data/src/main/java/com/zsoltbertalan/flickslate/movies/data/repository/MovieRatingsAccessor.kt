package com.zsoltbertalan.flickslate.movies.data.repository

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.movies.data.api.MovieRatingsDataSource
import com.zsoltbertalan.flickslate.movies.domain.api.MovieRatingsRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ActivityRetainedScoped
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
