package com.zsoltbertalan.flickslate.shared.util

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import com.zsoltbertalan.flickslate.shared.data.getresult.handle
import retrofit2.Response

/**
 * Executes a network a request, that returns a [retrofit2.Response].
 *
 * @param makeNetworkRequest The request
 * @param mapper, which discards the Response.
 *
 * @return [Outcome<DOMAIN>], where DOMAIN is NOT a [retrofit2.Response].
 */
suspend inline fun <REMOTE, DOMAIN> safeCall2(
	crossinline makeNetworkRequest: suspend () -> Response<REMOTE>,
	crossinline mapper: REMOTE.() -> DOMAIN,
): Outcome<DOMAIN> {
	return runCatchingApi {
		makeNetworkRequest()
	}.andThen { response ->
		if (response.isSuccessful) {
			Ok(response.body()!!)
		} else {
			Err(response.handle())
		}
	}.map {
		it.mapper()
	}
}

/**
 * Executes a network a request, that returns a [retrofit2.Response].
 *
 * @param makeNetworkRequest The request
 * @param mapper, which uses the Response metadata.
 *
 * @return [Outcome<DOMAIN>], where DOMAIN is a [retrofit2.Response].
 */
suspend inline fun <REMOTE, DOMAIN> safeCall(
	crossinline makeNetworkRequest: suspend () -> Response<REMOTE>,
	crossinline mapper: Response<REMOTE>.() -> DOMAIN,
): Outcome<DOMAIN> {
	return runCatchingApi {
		makeNetworkRequest()
	}.andThen { response ->
		if (response.isSuccessful) {
			Ok(response)
		} else {
			Err(response.handle())
		}
	}.map {
		it.mapper()
	}
}
