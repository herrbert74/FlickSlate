package com.zsoltbertalan.flickslate.movies.data.network

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SetMovieFavoriteService {

	@POST("account/{account_id}/favorite")
	suspend fun setMovieFavorite(
		@Path("account_id") accountId: Int,
		@Query("session_id") sessionId: String,
		@Body body: RequestBody,
	)

}
