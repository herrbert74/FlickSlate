package com.zsoltbertalan.flickslate.movies.data.network

import com.zsoltbertalan.flickslate.movies.data.api.MovieFavoritesDataSource
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
internal class MovieFavoritesRemoteDataSource @Inject constructor(
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
