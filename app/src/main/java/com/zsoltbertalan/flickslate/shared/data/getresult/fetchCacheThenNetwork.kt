package com.zsoltbertalan.flickslate.shared.data.getresult

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import com.github.michaelbull.result.recoverIf
import com.zsoltbertalan.flickslate.shared.domain.model.Failure
import com.zsoltbertalan.flickslate.shared.util.Outcome
import com.zsoltbertalan.flickslate.shared.util.runCatchingApi
import com.zsoltbertalan.flickslate.shared.util.runCatchingUnit
import com.zsoltbertalan.flickslate.shared.data.getresult.STRATEGY.CACHE_FIRST_NETWORK_LATER
import com.zsoltbertalan.flickslate.shared.data.getresult.STRATEGY.CACHE_FIRST_NETWORK_ONCE
import com.zsoltbertalan.flickslate.shared.data.getresult.STRATEGY.CACHE_FIRST_NETWORK_SECOND
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response

/**
 * A collection of generic Repository functions to handle common tasks like making network requests, fetching and
 * saving data from and to a database.
 *
 * Various caching strategies are handled by the different functions in this folder.
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
 * @param mapper - An extension function to convert [REMOTE] to [DOMAIN] format
 * @param strategy - How to deal with network requests and results. See [STRATEGY]
 * @return Result<[DOMAIN]> type
 */
inline fun <REMOTE, DOMAIN> fetchCacheThenNetwork(
	crossinline fetchFromLocal: () -> Flow<DOMAIN?>,
	crossinline shouldMakeNetworkRequest: (DOMAIN?) -> Boolean = { true },
	crossinline makeNetworkRequest: suspend () -> REMOTE,
	noinline saveResponseData: suspend (REMOTE) -> Unit = { },
	crossinline mapper: REMOTE.() -> DOMAIN,
	strategy: STRATEGY = CACHE_FIRST_NETWORK_SECOND,
) = flow<Outcome<DOMAIN>> {

	val localData = fetchFromLocal().first()
	localData?.let { emit(Ok(it)) }

	val networkOnlyOnceAndAlreadyCached = strategy == CACHE_FIRST_NETWORK_ONCE && localData != null

	if (shouldMakeNetworkRequest(localData) && networkOnlyOnceAndAlreadyCached.not()) {

		val result = runCatchingApi {
			makeNetworkRequest()
		}.andThen { dto ->
			saveResponseData(dto)
			Ok(dto)
		}.map {
			it.mapper()
		}.recoverIf(
			{ _ -> localData != null },
			{ null }
		)

		if (shouldEmitNetworkResult(result, strategy, localData == null)) {
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
 * @param mapper - An extension function to convert [REMOTE] to [DOMAIN] format
 * @param strategy - How to deal with network requests and results. See [STRATEGY]
 * @return Result<[DOMAIN]> type
 */
@Suppress("unused")
inline fun <REMOTE, DOMAIN> fetchCacheThenNetworkResponse(
	crossinline fetchFromLocal: () -> Flow<DOMAIN?>,
	crossinline shouldMakeNetworkRequest: (DOMAIN?) -> Boolean = { true },
	crossinline makeNetworkRequest: suspend () -> Response<REMOTE>,
	noinline saveResponseData: suspend (Response<REMOTE>) -> Unit = { },
	crossinline mapper: REMOTE.() -> DOMAIN,
	strategy: STRATEGY = CACHE_FIRST_NETWORK_SECOND,
) = flow<Outcome<DOMAIN>> {
	val localData = fetchFromLocal().first()
	localData?.let { emit(Ok(it)) }
	val networkOnlyOnceAndAlreadyCached = strategy == CACHE_FIRST_NETWORK_ONCE && localData != null
	if (shouldMakeNetworkRequest(localData) && networkOnlyOnceAndAlreadyCached.not()) {
		val result = runCatchingApi {
			makeNetworkRequest()
		}.andThen { response ->
			if (response.isSuccessful) {
				runCatchingUnit { saveResponseData(response) }
				Ok(response.body())
			} else {
				Err(response.handle())
			}
		}.map {
			it?.mapper()
		}.recoverIf(
			{ failure -> failure == Failure.NotModified || localData != null },
			{ null }
		)

		if (shouldEmitNetworkResult(result, strategy, localData == null)) {
			emitAll(
				flowOf(
					result.map { it!! }
				)
			)
		}
	}

}

fun <DOMAIN> shouldEmitNetworkResult(
	result: Outcome<DOMAIN?>,
	strategy: STRATEGY,
	isLocalNull: Boolean
): Boolean {
	return when (strategy) {
		CACHE_FIRST_NETWORK_LATER -> isLocalNull
		else -> result.isErr || (result.isOk && result.component1() != null)
	}
}

/**
 * Cache first strategies to deal with network requests and results.
 */
enum class STRATEGY {
	/**
	 * Always emit network result.
	 */
	CACHE_FIRST_NETWORK_SECOND,

	/**
	 * Save but do not emit network result, unless there is no cache result.
	 */
	CACHE_FIRST_NETWORK_LATER,

	/**
	 * Make network request only if there is no cache result.
	 */
	CACHE_FIRST_NETWORK_ONCE,
}
