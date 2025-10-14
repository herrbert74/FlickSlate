package com.zsoltbertalan.flickslate.tv.data.network

import com.zsoltbertalan.flickslate.account.data.api.AccountDataSource
import com.zsoltbertalan.flickslate.tv.data.api.TvRatingsDataSource
import com.zsoltbertalan.flickslate.shared.data.util.runCatchingApi
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
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
    private val accountDataSource: AccountDataSource.Local,
) : TvRatingsDataSource.Remote {

    override suspend fun rateTvShow(tvShowId: Int, rating: Float): Outcome<Unit> {
        return runCatchingApi {
            val sessionId = accountDataSource.getAccessToken() ?: throw Exception("User not logged in")
            tvService.rateTvShow(tvShowId, sessionId, createRatingRequestBody(rating))
        }
    }

    override suspend fun deleteTvShowRating(tvShowId: Int): Outcome<Unit> {
        return runCatchingApi {
            val sessionId = accountDataSource.getAccessToken() ?: throw Exception("User not logged in")
            tvService.deleteTvShowRating(tvShowId, sessionId)
        }
    }

    override suspend fun rateTvShowEpisode(tvShowId: Int, seasonNumber: Int, episodeNumber: Int, rating: Float): Outcome<Unit> {
        return runCatchingApi {
            val sessionId = accountDataSource.getAccessToken() ?: throw Exception("User not logged in")
            tvService.rateTvShowEpisode(tvShowId, seasonNumber, episodeNumber, sessionId, createRatingRequestBody(rating))
        }
    }

    override suspend fun deleteTvShowEpisodeRating(tvShowId: Int, seasonNumber: Int, episodeNumber: Int): Outcome<Unit> {
        return runCatchingApi {
            val sessionId = accountDataSource.getAccessToken() ?: throw Exception("User not logged in")
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
