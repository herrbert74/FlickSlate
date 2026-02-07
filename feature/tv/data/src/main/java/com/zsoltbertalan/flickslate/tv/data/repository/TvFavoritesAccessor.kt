package com.zsoltbertalan.flickslate.tv.data.repository

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import com.zsoltbertalan.flickslate.tv.data.api.TvFavoritesDataSource
import com.zsoltbertalan.flickslate.tv.domain.api.TvFavoritesRepository
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@ContributesBinding(ActivityRetainedScope::class)
@SingleIn(ActivityRetainedScope::class)
class TvFavoritesAccessor @Inject internal constructor(
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
