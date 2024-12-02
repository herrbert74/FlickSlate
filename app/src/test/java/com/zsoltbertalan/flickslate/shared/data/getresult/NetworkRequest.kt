package com.zsoltbertalan.flickslate.shared.data.getresult

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.shared.data.network.model.ErrorBody
import com.zsoltbertalan.flickslate.search.data.network.model.GenreReplyDto
import com.zsoltbertalan.flickslate.shared.model.Failure
import com.zsoltbertalan.flickslate.shared.model.Genre
import com.zsoltbertalan.flickslate.testhelper.GenreMother
import com.zsoltbertalan.flickslate.shared.util.Outcome
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

fun makeNetworkRequestResult(): () -> Outcome<List<Genre>> =
	{ Ok(GenreMother.createGenreList()) }

fun failNetworkRequestWithResultServerError(): () -> Outcome<List<Genre>> =
	{ Err(Failure.ServerError("Invalid id: The pre-requisite id is invalid or not found.")) }

fun failNetworkRequestWithResultNotModified(): () -> Outcome<List<Genre>> =
	{ Err(Failure.NotModified) }

fun makeNetworkRequestDelayedResponse(): suspend () -> Outcome<List<Genre>> = suspend {
	delay(1000)
	Ok(GenreMother.createGenreList())
}

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
