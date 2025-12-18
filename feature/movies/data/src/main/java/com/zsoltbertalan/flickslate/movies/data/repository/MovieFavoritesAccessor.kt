package com.zsoltbertalan.flickslate.movies.data.repository

import com.zsoltbertalan.flickslate.movies.data.api.MovieFavoritesDataSource
import com.zsoltbertalan.flickslate.movies.domain.api.MovieFavoritesRepository
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import dagger.hilt.android.scopes.ActivityRetainedScoped
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ActivityRetainedScoped
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
