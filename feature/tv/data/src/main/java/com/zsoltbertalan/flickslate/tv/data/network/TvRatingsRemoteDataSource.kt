package com.zsoltbertalan.flickslate.tv.data.network

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.shared.data.util.runCatchingApi
import com.zsoltbertalan.flickslate.tv.data.api.TvRatingsDataSource
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class TvRatingsRemoteDataSource @Inject internal constructor(
	private val tvService: TvService,
) : TvRatingsDataSource.Remote {

	override suspend fun rateTvShow(tvShowId: Int, rating: Float, sessionId: String): Outcome<Unit> {
		return runCatchingApi {
			tvService.rateTvShow(tvShowId, sessionId, createRatingRequestBody(rating))
		}
	}

	override suspend fun deleteTvShowRating(tvShowId: Int, sessionId: String): Outcome<Unit> {
		return runCatchingApi {
			tvService.deleteTvShowRating(tvShowId, sessionId)
		}
	}

	override suspend fun rateTvShowEpisode(
		tvShowId: Int,
		seasonNumber: Int,
		episodeNumber: Int,
		rating: Float,
		sessionId: String
	): Outcome<Unit> {
		return runCatchingApi {
			tvService.rateTvShowEpisode(
				tvShowId,
				seasonNumber,
				episodeNumber,
				sessionId,
				createRatingRequestBody(rating)
			)
		}
	}

	override suspend fun deleteTvShowEpisodeRating(
		tvShowId: Int,
		seasonNumber: Int,
		episodeNumber: Int,
		sessionId: String
	): Outcome<Unit> {
		return runCatchingApi {
			tvService.deleteTvShowEpisodeRating(tvShowId, seasonNumber, episodeNumber, sessionId)
		}
	}
}

private fun createRatingRequestBody(rating: Float): RequestBody {
	val body = buildJsonObject {
		put("value", rating)
	}
	return body.toString().toRequestBody("application/json; charset=UTF-8".toMediaType())
}
