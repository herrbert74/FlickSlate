package com.zsoltbertalan.flickslate.movies.data.repository

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.movies.data.api.MovieFavoritesDataSource
import com.zsoltbertalan.flickslate.movies.domain.api.MovieFavoritesRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class MovieFavoritesAccessor @Inject internal constructor(
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
