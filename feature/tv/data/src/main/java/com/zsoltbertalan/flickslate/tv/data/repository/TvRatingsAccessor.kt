package com.zsoltbertalan.flickslate.tv.data.repository

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.tv.data.api.TvRatingsDataSource
import com.zsoltbertalan.flickslate.tv.domain.api.TvRatingsRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
@Inject
class TvRatingsAccessor internal constructor(
	private val tvRatingsRemoteDataSource: TvRatingsDataSource.Remote,
) : TvRatingsRepository {

	override suspend fun rateTvShow(tvShowId: Int, rating: Float, sessionId: String): Outcome<Unit> {
		return tvRatingsRemoteDataSource.rateTvShow(tvShowId, rating, sessionId)
	}

	override suspend fun deleteTvShowRating(tvShowId: Int, sessionId: String): Outcome<Unit> {
		return tvRatingsRemoteDataSource.deleteTvShowRating(tvShowId, sessionId)
	}

	override suspend fun rateTvShowEpisode(
		tvShowId: Int,
		seasonNumber: Int,
		episodeNumber: Int,
		rating: Float,
		sessionId: String
	): Outcome<Unit> {
		return tvRatingsRemoteDataSource.rateTvShowEpisode(tvShowId, seasonNumber, episodeNumber, rating, sessionId)
	}

	override suspend fun deleteTvShowEpisodeRating(
		tvShowId: Int,
		seasonNumber: Int,
		episodeNumber: Int,
		sessionId: String
	): Outcome<Unit> {
		return tvRatingsRemoteDataSource.deleteTvShowEpisodeRating(tvShowId, seasonNumber, episodeNumber, sessionId)
	}

}
