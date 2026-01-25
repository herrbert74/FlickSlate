package com.zsoltbertalan.flickslate.tv.domain.api

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome

interface TvRatingsRepository {

	suspend fun rateTvShow(tvShowId: Int, rating: Float, sessionId: String): Outcome<Unit>

	suspend fun deleteTvShowRating(tvShowId: Int, sessionId: String): Outcome<Unit>

	suspend fun rateTvShowEpisode(
		tvShowId: Int,
		seasonNumber: Int,
		episodeNumber: Int,
		rating: Float,
		sessionId: String
	): Outcome<Unit>

	suspend fun deleteTvShowEpisodeRating(
		tvShowId: Int,
		seasonNumber: Int,
		episodeNumber: Int,
		sessionId: String
	): Outcome<Unit>

}
