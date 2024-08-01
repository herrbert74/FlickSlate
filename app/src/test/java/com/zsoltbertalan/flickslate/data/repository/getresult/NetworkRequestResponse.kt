package com.zsoltbertalan.flickslate.data.repository.getresult

import com.zsoltbertalan.flickslate.common.testhelper.GenreDtoMother
import com.zsoltbertalan.flickslate.data.network.dto.ErrorBody
import com.zsoltbertalan.flickslate.data.network.dto.GenreResponse
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

fun makeNetworkRequestResponse(): () -> Response<GenreResponse> =
	{ Response.success(GenreResponse(GenreDtoMother.createGenreDtoList())) }

suspend fun makeNetworkRequestDelayedResponse(): suspend () -> Response<GenreResponse> = suspend {
	delay(1000)
	Response.success(GenreResponse(GenreDtoMother.createGenreDtoList()))
}

/**
 * This is used for both non-Response fetchers and Response fetchers wrapped in a HttpException.
 */
fun failNetworkRequestResponse(): () -> Response<GenreResponse> = {
	val errorBody = ErrorBody(
		success = false,
		status_code = 6,
		status_message = "Invalid id: The pre-requisite id is invalid or not found."
	)
	Response.error(404, Json.encodeToString(errorBody).toResponseBody())
}

fun failNetworkRequestWithResponse(): () -> Response<GenreResponse> =
	{ throw HttpException(failNetworkRequestResponse()()) }
