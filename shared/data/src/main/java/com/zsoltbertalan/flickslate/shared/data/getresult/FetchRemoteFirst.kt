package com.zsoltbertalan.flickslate.shared.data.getresult

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import com.github.michaelbull.result.recoverIf
import com.github.michaelbull.retry.policy.RetryPolicy
import com.zsoltbertalan.flickslate.shared.data.retry.retry
import com.zsoltbertalan.flickslate.shared.data.util.runCatchingUnit
import com.zsoltbertalan.flickslate.shared.kotlin.result.Failure
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

/**
 * A generic Repository function to handle network first strategy as described here:
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
 * @param makeNetworkRequest A function to make network request and retrieve data mapped to [DOMAIN].
 * [retrofit2.Response] can be used as part of the call. It is needed when we want to extract information from the
 * header (like ETags) or the error body.
 *
 * @param saveResponseData A function to save network reply coming in [DOMAIN] format from [makeNetworkRequest]
 *
 * @return Result<[DOMAIN]> type
 */
inline fun <DOMAIN> fetchRemoteFirst(
	crossinline fetchFromLocal: () -> Flow<DOMAIN?>,
	crossinline makeNetworkRequest: suspend () -> Outcome<DOMAIN>,
	noinline saveResponseData: suspend (DOMAIN) -> Unit = { },
	retryPolicy: RetryPolicy<Failure> = defaultNoRetryPolicy,
) = flow<Outcome<DOMAIN>> {
	var localData: DOMAIN? = null

	val result = retry(retryPolicy) { makeNetworkRequest() }.andThen { domain ->
		runCatchingUnit { saveResponseData(domain) }
		Ok(domain)
	}.recoverIf(
		{ failure ->
			localData = fetchFromLocal().first()
			failure == Failure.NotModified || localData != null
		},
		{ localData }
	)

	if (result.isErr || (result.isOk && result.component1() != null)) {
		emitAll(
			flowOf(
				result.map { it!! }
			)
		)
	}
}
