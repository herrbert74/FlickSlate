package com.zsoltbertalan.flickslate.shared.data.getresult

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import com.github.michaelbull.result.recoverIf
import com.github.michaelbull.retry.policy.RetryPolicy
import com.github.michaelbull.retry.policy.binaryExponentialBackoff
import com.github.michaelbull.retry.policy.constantDelay
import com.github.michaelbull.retry.policy.continueIf
import com.github.michaelbull.retry.policy.plus
import com.github.michaelbull.retry.policy.stopAtAttempts
import com.zsoltbertalan.flickslate.shared.data.getresult.STRATEGY.CACHE_FIRST_NETWORK_LATER
import com.zsoltbertalan.flickslate.shared.data.getresult.STRATEGY.CACHE_FIRST_NETWORK_ONCE
import com.zsoltbertalan.flickslate.shared.data.getresult.STRATEGY.CACHE_FIRST_NETWORK_SECOND
import com.zsoltbertalan.flickslate.shared.data.retry.retry
import com.zsoltbertalan.flickslate.shared.model.Failure
import com.zsoltbertalan.flickslate.shared.util.Outcome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

const val RETRY_DELAY = 5000L
const val RETRY_ATTEMPTS = 5

/**
 * There are four retry policies, which can be combined through the plus operator.
 * They are: Backoff, Predicate, Delay and Stop. I tried to add at least one each to the below examples.
 * See https://github.com/michaelbull/kotlin-retry/blob/master/kotlin-retry/src/commonMain/kotlin/com/github/michaelbull/retry/policy/Backoff.kt
 */
val defaultNoRetryPolicy = continueIf<Failure> { false }
val defaultRetryPolicy = constantDelay<Failure>(RETRY_DELAY) + stopAtAttempts(RETRY_ATTEMPTS)
val backoffRetryPolicy = binaryExponentialBackoff<Failure>(min = 10L, max = 5000L) + stopAtAttempts(RETRY_ATTEMPTS)

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
 * @param shouldMakeNetworkRequest Whether or not to make network request.
 *
 * @param makeNetworkRequest A function to make network request and retrieve data mapped to [DOMAIN].
 * [retrofit2.Response] can be used as part of the call. It is needed when we want to extract information from the
 * header (like ETags) or the error body.
 *
 * @param saveResponseData A function to save network reply coming in [DOMAIN] format from [makeNetworkRequest].
 *
 * @param strategy How to deal with network requests and results. See [STRATEGY].
 *
 * @param retryPolicy How to do retries after a failed network request. Default is to do nothing. See [RetryPolicy].
 *
 * @return Result<[DOMAIN]> type
 */
inline fun <DOMAIN> fetchCacheThenRemote(
	crossinline fetchFromLocal: () -> Flow<DOMAIN?>,
	crossinline shouldMakeNetworkRequest: (DOMAIN?) -> Boolean = { true },
	crossinline makeNetworkRequest: suspend () -> Outcome<DOMAIN>,
	noinline saveResponseData: suspend (DOMAIN) -> Unit = { },
	strategy: STRATEGY = CACHE_FIRST_NETWORK_SECOND,
	retryPolicy: RetryPolicy<Failure> = defaultNoRetryPolicy,
) = flow<Outcome<DOMAIN>> {

	val localData = fetchFromLocal().first()
	localData?.let { emit(Ok(it)) }

	val networkOnlyOnceAndAlreadyCached = strategy == CACHE_FIRST_NETWORK_ONCE && localData != null

	if (shouldMakeNetworkRequest(localData) && networkOnlyOnceAndAlreadyCached.not()) {

		val newResult = retry(retryPolicy) { makeNetworkRequest() }.andThen { domain ->
			saveResponseData(domain)
			Ok(domain)
		}.recoverIf(
			{ failure ->
				failure == Failure.NotModified || localData != null
			},
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
