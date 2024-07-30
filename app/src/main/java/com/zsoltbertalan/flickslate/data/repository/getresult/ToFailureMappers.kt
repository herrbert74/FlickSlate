package com.zsoltbertalan.flickslate.data.repository.getresult

import com.zsoltbertalan.flickslate.domain.model.Failure
import okhttp3.internal.http.HTTP_BAD_REQUEST
import okhttp3.internal.http.HTTP_NOT_FOUND
import okhttp3.internal.http.HTTP_NOT_MODIFIED
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

/**
 * Handle only expected Exceptions, throw Errors and other Exceptions, like CancellationException
 */
fun Throwable.handle() = when (this) {
	is UnknownHostException -> Failure.UnknownHostFailure
	is HttpException -> Failure.ServerError
	is IOException -> Failure.IoFailure
	else -> throw this
}

/**
 * Handle all unsuccessful Responses.
 */
fun <T> Response<T>.handleCode() = when {
	this.code() == HTTP_BAD_REQUEST -> Failure.ServerError
	this.code() == HTTP_NOT_FOUND -> Failure.ServerError
	this.code() == HTTP_NOT_MODIFIED -> Failure.NotModified
	else -> Failure.UnknownApiError
}