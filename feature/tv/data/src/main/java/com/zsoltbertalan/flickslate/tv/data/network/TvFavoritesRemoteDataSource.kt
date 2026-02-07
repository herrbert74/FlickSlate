package com.zsoltbertalan.flickslate.tv.data.network

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.shared.data.util.runCatchingApi
import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import com.zsoltbertalan.flickslate.tv.data.api.TvFavoritesDataSource
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@ContributesBinding(ActivityRetainedScope::class)
@SingleIn(ActivityRetainedScope::class)
internal class TvFavoritesRemoteDataSource @Inject constructor(
	private val setTvFavoriteService: SetTvFavoriteService,
) : TvFavoritesDataSource.Remote {

	override suspend fun setTvFavorite(
		accountId: Int,
		sessionId: String,
		tvId: Int,
		favorite: Boolean,
	): Outcome<Unit> {
		return runCatchingApi {
			setTvFavoriteService.setTvFavorite(
				accountId,
				sessionId,
				createSetTvFavoriteRequestBody(tvId, favorite),
			)
		}
	}

}

private fun createSetTvFavoriteRequestBody(tvId: Int, favorite: Boolean): RequestBody {
	val body = buildJsonObject {
		put("media_type", "tv")
		put("media_id", tvId)
		put("favorite", favorite)
	}
	return body.toString().toRequestBody("application/json; charset=UTF-8".toMediaType())
}
