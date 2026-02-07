package com.zsoltbertalan.flickslate.movies.data.network

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.movies.data.api.MovieFavoritesDataSource
import com.zsoltbertalan.flickslate.shared.data.util.runCatchingApi
import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
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
class MovieFavoritesRemoteDataSource @Inject internal constructor(
	private val setMovieFavoriteService: SetMovieFavoriteService,
) : MovieFavoritesDataSource.Remote {

	override suspend fun setMovieFavorite(
		accountId: Int,
		sessionId: String,
		movieId: Int,
		favorite: Boolean,
	): Outcome<Unit> {
		return runCatchingApi {
			setMovieFavoriteService.setMovieFavorite(
				accountId,
				sessionId,
				createSetMovieFavoriteRequestBody(movieId, favorite),
			)
		}
	}

}

private fun createSetMovieFavoriteRequestBody(movieId: Int, favorite: Boolean): RequestBody {
	val body = buildJsonObject {
		put("media_type", "movie")
		put("media_id", movieId)
		put("favorite", favorite)
	}
	return body.toString().toRequestBody("application/json; charset=UTF-8".toMediaType())
}
