package com.zsoltbertalan.flickslate.movies.data.network

import com.zsoltbertalan.flickslate.movies.data.api.MovieRatingsDataSource
import com.zsoltbertalan.flickslate.shared.data.util.runCatchingApi
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
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
internal class MovieRatingsRemoteDataSource @Inject constructor(
	private val moviesService: MoviesService,
) : MovieRatingsDataSource.Remote {

	override suspend fun rateMovie(movieId: Int, rating: Float, sessionId: String): Outcome<Unit> {
		return runCatchingApi {
			moviesService.rateMovie(movieId, sessionId, createRatingRequestBody(rating))
		}
	}

	override suspend fun deleteMovieRating(movieId: Int, sessionId: String): Outcome<Unit> {
		return runCatchingApi {
			moviesService.deleteMovieRating(movieId, sessionId)
		}
	}
}

private fun createRatingRequestBody(rating: Float): RequestBody {
	val body = buildJsonObject {
		put("value", rating)
	}
	return body.toString().toRequestBody("application/json; charset=UTF-8".toMediaType())
}
