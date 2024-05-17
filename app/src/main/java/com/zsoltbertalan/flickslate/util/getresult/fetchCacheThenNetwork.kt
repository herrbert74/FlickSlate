package com.zsoltbertalan.flickslate.util.getresult

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.recoverIf
import com.zsoltbertalan.flickslate.ext.ApiResult
import com.zsoltbertalan.flickslate.ext.apiRunCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import okhttp3.internal.http.HTTP_NOT_MODIFIED
import retrofit2.HttpException
import retrofit2.Response

/**
 * A collection of generic Repository functions to handle common tasks like making network requests, fetching and
 * saving data from and to a database.
 *
 * Various caching strategies are handled by the different functions in this file (Not fully implemented yet).
 *
 * Based on Flower:
 * https://github.com/hadiyarajesh/flower/blob/master/flower-core/src/commonMain/kotlin/com/hadiyarajesh/flower_core/DBBoundResource.kt
 *
 * Some unused function parameters were removed.
 *
 * KotlinResult library is used instead of custom monad. Loading should be handled by UI.
 *
 * REMOTE class is handled by Repository due to Retrofit cannot handle mapping, but DB classes are
 * handled only by database, as we have full control and it can handle DOMAIN classes.
 *
 * The retrofit2.Response class is used to handle Etags and other header elements, as well as error bodies.
 */

/**
 * For [retrofit2.Response] version see [fetchCacheThenNetworkResponse]. Response is needed when we want to extract
 * information from the header (like eTags) or the error body.
 *
 * Recovers from network errors if the cache contains any data by emitting null.
 *
 * Fetch the data from local database (if available),
 * emit if it's available,
 * perform a network request (if instructed),
 * and if not null, emit the response after saving it to local database.
 * @param fetchFromLocal - A function to retrieve [DOMAIN] data from local database. Must be nullable!
 * @param shouldMakeNetworkRequest - Whether or not to make network request
 * @param makeNetworkRequest - A function to make network request and retrieve [REMOTE] data
 * @param saveResponseData - A function to save network reply coming in [REMOTE] format
 * @return Result<[DOMAIN]> type
 */
inline fun <REMOTE, DOMAIN> fetchCacheThenNetwork(
	crossinline fetchFromLocal: () -> Flow<DOMAIN?>,
	crossinline shouldMakeNetworkRequest: (DOMAIN?) -> Boolean = { true },
	crossinline makeNetworkRequest: suspend () -> REMOTE,
	crossinline saveResponseData: suspend (REMOTE) -> Unit = { },
) = flow<ApiResult<DOMAIN>> {

	val localData = fetchFromLocal().first()
	localData?.let { emit(Ok(it)) }
	if (shouldMakeNetworkRequest(localData)) {
		val result = apiRunCatching {
			makeNetworkRequest()
		}.andThen { dto ->
			saveResponseData(dto)
			Ok(dto)
		}.andThen {
			Ok(fetchFromLocal().first())
		}.recoverIf(
			{ _ -> localData != null },
			{ null }
		).mapError {
			it
		}
		if (result is Err || (result is Ok && result.component1() != null)) {
			emitAll(
				flowOf(
					result.map { it!! }
				)
			)
		}
	}
}

/**
 * [retrofit2.Response] version of [fetchCacheThenNetwork]. Response is needed when we want to extract information
 * from the header (like eTags) or the error body. Otherwise use the simpler version above.
 *
 * Recovers from not modified HTTP response if we add eTag through @Header("If-None-Match") to the request, and it
 * there is no update, or if the cache contains any data. In these cases it recovers by emitting null.
 *
 * Fetch the data from local database (if available),
 * emit if it's available,
 * perform a network request (if instructed),
 * and if not null, emit the response after saving it to local database.
 * @param fetchFromLocal - A function to retrieve [DOMAIN] data from local database. Must be nullable!
 * @param shouldMakeNetworkRequest - Whether or not to make network request
 * @param makeNetworkRequest - A function to make network request and retrieve [REMOTE] data wrapped in [retrofit2.Response]
 * @param saveResponseData - A function to save network reply coming in [REMOTE] format wrapped in [retrofit2.Response]
 * @return Result<[DOMAIN]> type
 */
@Suppress("unused")
inline fun <REMOTE, DOMAIN> fetchCacheThenNetworkResponse(
	crossinline fetchFromLocal: () -> Flow<DOMAIN?>,
	crossinline shouldMakeNetworkRequest: (DOMAIN?) -> Boolean = { true },
	crossinline makeNetworkRequest: suspend () -> Response<REMOTE>,
	crossinline saveResponseData: suspend (Response<REMOTE>) -> Unit = { },
) = flow<ApiResult<DOMAIN>> {

	val localData = fetchFromLocal().first()
	localData?.let { emit(Ok(it)) }
	if (shouldMakeNetworkRequest(localData)) {
		val result = apiRunCatching {
			makeNetworkRequest()
		}.andThen { dto ->
			saveResponseData(dto)
			Ok(dto)
		}.andThen {
			Ok(fetchFromLocal().first())
		}.recoverIf(
			{ throwable -> (throwable as? HttpException)?.code() == HTTP_NOT_MODIFIED || localData != null },
			{ null }
		).mapError {
			it
		}
		if (result is Err || (result is Ok && result.component1() != null)) {
			emitAll(
				flowOf(
					result.map { it!! }
				)
			)
		}
	}

}
