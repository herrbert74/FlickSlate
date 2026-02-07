package com.zsoltbertalan.flickslate.tv.data.network

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

internal interface SetTvFavoriteService {

	@POST("account/{account_id}/favorite")
	suspend fun setTvFavorite(
		@Path("account_id") accountId: Int,
		@Query("session_id") sessionId: String,
		@Body body: RequestBody,
	)

}
