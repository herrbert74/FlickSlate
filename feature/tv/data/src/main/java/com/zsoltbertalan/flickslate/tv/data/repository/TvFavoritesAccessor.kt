package com.zsoltbertalan.flickslate.tv.data.repository

import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.tv.data.api.TvFavoritesDataSource
import com.zsoltbertalan.flickslate.tv.domain.api.TvFavoritesRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ActivityRetainedScoped
internal class TvFavoritesAccessor @Inject constructor(
	private val tvFavoritesRemoteDataSource: TvFavoritesDataSource.Remote,
) : TvFavoritesRepository {

	override suspend fun setTvFavorite(
		accountId: Int,
		sessionId: String,
		tvId: Int,
		favorite: Boolean,
	): Outcome<Unit> {
		return tvFavoritesRemoteDataSource.setTvFavorite(accountId, sessionId, tvId, favorite)
	}

}
