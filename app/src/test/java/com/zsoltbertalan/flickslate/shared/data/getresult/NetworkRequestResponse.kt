package com.zsoltbertalan.flickslate.shared.data.getresult

import com.zsoltbertalan.flickslate.shared.testhelper.GenreDtoMother
import com.zsoltbertalan.flickslate.data.network.model.ErrorBody
import com.zsoltbertalan.flickslate.search.data.network.model.GenreReplyDto
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

fun makeNetworkRequestResponse(): () -> Response<GenreReplyDto> =
	{ Response.success(GenreReplyDto(GenreDtoMother.createGenreDtoList())) }

suspend fun makeNetworkRequestDelayedResponse(): suspend () -> Response<GenreReplyDto> = suspend {
	delay(1000)
	Response.success(GenreReplyDto(GenreDtoMother.createGenreDtoList()))
}

/**
 * This is used for both non-Response fetchers and Response fetchers wrapped in a HttpException.
 */
fun failNetworkRequestResponse(): () -> Response<GenreReplyDto> = {
	val errorBody = ErrorBody(
		success = false,
		status_code = 6,
		status_message = "Invalid id: The pre-requisite id is invalid or not found."
	)
	Response.error(404, Json.encodeToString(errorBody).toResponseBody())
}

fun failNetworkRequestWithResponse(): () -> Response<GenreReplyDto> =
	{ throw HttpException(failNetworkRequestResponse()()) }
