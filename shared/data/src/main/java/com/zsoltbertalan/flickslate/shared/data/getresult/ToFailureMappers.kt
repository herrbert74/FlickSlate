package com.zsoltbertalan.flickslate.shared.data.getresult

import com.zsoltbertalan.flickslate.shared.data.network.model.ErrorBody
import com.zsoltbertalan.flickslate.base.kotlin.result.Failure
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection.HTTP_NOT_MODIFIED
import java.net.UnknownHostException

/**
 * Handle only expected Exceptions, throw Errors and other Exceptions, like CancellationException
 */
fun Throwable.handle() = when (this) {
	is UnknownHostException -> Failure.UnknownHostFailure
	is HttpException -> {
		val errorJson = this.response()?.errorBody()?.string() ?: ""
		val errorBody = Json.decodeFromString<ErrorBody>(errorJson)
		Failure.ServerError(errorBody.status_message)
	}
	is IOException -> Failure.IoFailure
	else -> throw this
}

/**
 * Handle all unsuccessful Responses.
 */
fun <T> Response<T>.handle() = when {
	this.code() == HTTP_NOT_MODIFIED -> Failure.NotModified
	else -> {
		val errorJson = this.errorBody()?.string() ?: ""
		Failure.ServerError(errorJson)
	}
}
