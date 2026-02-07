package com.zsoltbertalan.flickslate.movies.data.repository

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.movies.data.api.MovieFavoritesDataSource
import com.zsoltbertalan.flickslate.movies.domain.api.MovieFavoritesRepository
import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import javax.inject.Inject

@ContributesBinding(ActivityRetainedScope::class)
@SingleIn(ActivityRetainedScope::class)
internal class MovieFavoritesAccessor @Inject constructor(
	private val movieFavoritesRemoteDataSource: MovieFavoritesDataSource.Remote,
) : MovieFavoritesRepository {

	override suspend fun setMovieFavorite(
		accountId: Int,
		sessionId: String,
		movieId: Int,
		favorite: Boolean,
	): Outcome<Unit> {
		return movieFavoritesRemoteDataSource.setMovieFavorite(accountId, sessionId, movieId, favorite)
	}

}
