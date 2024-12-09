package com.zsoltbertalan.flickslate.search.data.network

import com.zsoltbertalan.flickslate.search.data.network.model.GenreReplyDto
import com.zsoltbertalan.flickslate.shared.data.network.model.ErrorBody
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

/**
 * TODO
 * This can be used to test [com.zsoltbertalan.flickslate.search.data.network.GenreRemoteDataSource]
 */
@Suppress("unused")
fun failNetworkRequestResponse(): () -> Response<GenreReplyDto> = {
	val errorBody = ErrorBody(
		success = false,
		status_code = 6,
		status_message = "Invalid id: The pre-requisite id is invalid or not found."
	)
	Response.error(404, Json.encodeToString(errorBody).toResponseBody())
}
