package com.zsoltbertalan.flickslate

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.tv.domain.api.TvRatingsRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class FakeTvRatingsRepository @Inject constructor() : TvRatingsRepository {

	private var latestRating: Float = -1f

	override suspend fun rateTvShow(tvShowId: Int, rating: Float, sessionId: String): Outcome<Unit> {
		latestRating = rating
		return Ok(Unit)
	}

	override suspend fun deleteTvShowRating(tvShowId: Int, sessionId: String): Outcome<Unit> {
		latestRating = -1f
		return Ok(Unit)
	}

	override suspend fun rateTvShowEpisode(
		tvShowId: Int,
		seasonNumber: Int,
		episodeNumber: Int,
		rating: Float,
		sessionId: String
	): Outcome<Unit> = Ok(Unit)

	override suspend fun deleteTvShowEpisodeRating(
		tvShowId: Int,
		seasonNumber: Int,
		episodeNumber: Int,
		sessionId: String
	): Outcome<Unit> = Ok(Unit)
}
