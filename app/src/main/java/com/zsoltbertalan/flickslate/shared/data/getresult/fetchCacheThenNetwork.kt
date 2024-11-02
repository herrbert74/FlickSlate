package com.zsoltbertalan.flickslate.shared.data.getresult

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import com.github.michaelbull.result.recoverIf
import com.zsoltbertalan.flickslate.shared.data.getresult.STRATEGY.CACHE_FIRST_NETWORK_LATER
import com.zsoltbertalan.flickslate.shared.data.getresult.STRATEGY.CACHE_FIRST_NETWORK_ONCE
import com.zsoltbertalan.flickslate.shared.data.getresult.STRATEGY.CACHE_FIRST_NETWORK_SECOND
import com.zsoltbertalan.flickslate.shared.domain.model.Failure
import com.zsoltbertalan.flickslate.shared.util.Outcome
import com.zsoltbertalan.flickslate.shared.util.runCatchingApi
import com.zsoltbertalan.flickslate.shared.util.runCatchingUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response

/**
 * A generic Repository function to handle **cache first** strategy as described here:
 * https://herrbert74.github.io/posts/caching-strategies-in-android/#network-first-aka-stale-if-error
 *
 * Recovers from not modified HTTP response if we add eTag through @Header("If-None-Match") to the request, and if
 * there is no update, or if the cache contains any data. In these cases it recovers by emitting null.
 *
 * Based on Flower:
 * https://github.com/hadiyarajesh/flower/blob/master/flower-core/src/commonMain/kotlin/com/hadiyarajesh/flower_core/DBBoundResource.kt
 *
 * Difference to Flower
 * Some unused function parameters were removed.
 * KotlinResult library is used instead of custom monad. Loading should be handled by UI.
 *
 * @param fetchFromLocal A function to retrieve [DOMAIN] data from local database. Must be nullable!
 *
 * @param shouldMakeNetworkRequest Whether or not to make network request
 *
 * @param makeNetworkRequest A function to make network request and retrieve data mapped to [DOMAIN].
 * [retrofit2.Response] can be used as part of the call. It is needed when we want to extract information from the
 * header (like ETags) or the error body.
 *
 * @param saveResponseData A function to save network reply coming in [DOMAIN] format from [makeNetworkRequest]
 *
 * @param strategy How to deal with network requests and results. See [STRATEGY]
 *
 * @return Result<[DOMAIN]> type
 */
inline fun <DOMAIN> fetchCacheThenRemote(
	crossinline fetchFromLocal: () -> Flow<DOMAIN?>,
	crossinline shouldMakeNetworkRequest: (DOMAIN?) -> Boolean = { true },
	crossinline makeNetworkRequest: suspend () -> Outcome<DOMAIN>,
	noinline saveResponseData: suspend (DOMAIN) -> Unit = { },
	strategy: STRATEGY = CACHE_FIRST_NETWORK_SECOND,
) = flow<Outcome<DOMAIN>> {

	val localData = fetchFromLocal().first()
	localData?.let { emit(Ok(it)) }

	val networkOnlyOnceAndAlreadyCached = strategy == CACHE_FIRST_NETWORK_ONCE && localData != null

	println("f: $networkOnlyOnceAndAlreadyCached")
	if (shouldMakeNetworkRequest(localData) && networkOnlyOnceAndAlreadyCached.not()) {

		val newResult = makeNetworkRequest().andThen { domain ->
			saveResponseData(domain)
			Ok(domain)
		}.recoverIf(
			{ failure ->
				failure == Failure.NotModified || localData != null },
			{ null }
		)

		if (shouldEmitNetworkResult(newResult, strategy, localData == null)) {
			emitAll(
				flowOf(
					newResult.map { it!! }
				)
			)
		}
	}
}

/**
 * For [retrofit2.Response] version see [fetchCacheThenNetworkResponse]. Response is needed when we want to extract
 * information from the header (like ETags) or the error body.
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
 * from the header (like ETags) or the error body. Otherwise use the simpler version above.
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
