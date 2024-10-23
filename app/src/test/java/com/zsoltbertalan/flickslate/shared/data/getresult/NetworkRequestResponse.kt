package com.zsoltbertalan.flickslate.shared.data.getresult

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.shared.testhelper.GenreDtoMother
import com.zsoltbertalan.flickslate.shared.data.network.model.ErrorBody
import com.zsoltbertalan.flickslate.search.data.network.model.GenreReplyDto
import com.zsoltbertalan.flickslate.shared.domain.model.Failure
import com.zsoltbertalan.flickslate.shared.domain.model.Genre
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.testhelper.GenreMother
import com.zsoltbertalan.flickslate.shared.util.Outcome
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

fun makeNetworkRequestResult(): () -> Outcome<PagingReply<Genre>> =
	{ Ok(PagingReply(GenreMother.createGenreList(), false, PageData())) }

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

fun failNetworkRequestWithResultServerError(): () -> Outcome<List<Genre>> =
	{ Err(Failure.ServerError("Invalid id: The pre-requisite id is invalid or not found.")) }

fun failNetworkRequestWithResultNotModified(): () -> Outcome<List<Genre>> =
	{ Err(Failure.NotModified) }

fun failNetworkRequestWithResponse(): () -> Response<GenreReplyDto> =
	{ throw HttpException(failNetworkRequestResponse()()) }
