package com.zsoltbertalan.flickslate.data.repository.getresult

import com.zsoltbertalan.flickslate.common.testhelper.GenreDtoMother
import com.zsoltbertalan.flickslate.data.network.dto.ErrorBody
import com.zsoltbertalan.flickslate.data.network.dto.GenreReply
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

fun makeNetworkRequestResponse(): () -> Response<GenreReply> =
	{ Response.success(GenreReply(GenreDtoMother.createGenreDtoList())) }

suspend fun makeNetworkRequestDelayedResponse(): suspend () -> Response<GenreReply> = suspend {
	delay(1000)
	Response.success(GenreReply(GenreDtoMother.createGenreDtoList()))
}

/**
 * This is used for both non-Response fetchers and Response fetchers wrapped in a HttpException.
 */
fun failNetworkRequestResponse(): () -> Response<GenreReply> = {
	val errorBody = ErrorBody(
		success = false,
		status_code = 6,
		status_message = "Invalid id: The pre-requisite id is invalid or not found."
	)
	Response.error(404, Json.encodeToString(errorBody).toResponseBody())
}

fun failNetworkRequestWithResponse(): () -> Response<GenreReply> =
	{ throw HttpException(failNetworkRequestResponse()()) }
