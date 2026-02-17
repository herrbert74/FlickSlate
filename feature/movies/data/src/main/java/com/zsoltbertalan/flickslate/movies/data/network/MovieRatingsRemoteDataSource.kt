package com.zsoltbertalan.flickslate.movies.data.network

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.movies.data.api.MovieRatingsDataSource
import com.zsoltbertalan.flickslate.shared.data.util.runCatchingApi
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
@Inject
class MovieRatingsRemoteDataSource internal constructor(
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
