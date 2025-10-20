package com.zsoltbertalan.flickslate.tv.data.network

import com.zsoltbertalan.flickslate.shared.data.util.runCatchingApi
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.tv.data.api.TvRatingsDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ActivityRetainedScoped
internal class TvRatingsRemoteDataSource @Inject constructor(
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
