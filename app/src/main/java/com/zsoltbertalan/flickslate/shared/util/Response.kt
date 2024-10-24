package com.zsoltbertalan.flickslate.shared.util

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import com.zsoltbertalan.flickslate.shared.data.getresult.handle
import retrofit2.Response

/**
 * Executes a network request, that returns a [retrofit2.Response].
 *
 * @param makeNetworkRequest The request
 * @param mapper, which DOES NOT use the Response.
 *
 * @return [Outcome] where [DOMAIN] does NOT contain metadata from a [retrofit2.Response].
 */
suspend inline fun <REMOTE, DOMAIN> safeCall(
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
 * Executes a network request, that returns a [retrofit2.Response].
 *
 * @param makeNetworkRequest The request
 * @param mapper, which uses the Response metadata.
 *
 * @return [Outcome], where [DOMAIN] contains metadata from a [retrofit2.Response]. This can be used to save it to
 * the database or use it in the Presentation layer.
 *
 * The downside of this is that the metadata is exposed to the domain layer. To mitigate this another model layer
 * should be introduced, if the amount of the data in the domain (or presentation) is a concern.
 */
suspend inline fun <REMOTE, DOMAIN> safeCallWithMetadata(
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
