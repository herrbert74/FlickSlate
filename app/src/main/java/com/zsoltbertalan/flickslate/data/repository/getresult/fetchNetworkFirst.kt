package com.zsoltbertalan.flickslate.data.repository.getresult

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import com.github.michaelbull.result.recoverIf
import com.zsoltbertalan.flickslate.domain.model.Failure
import com.zsoltbertalan.flickslate.common.util.Outcome
import com.zsoltbertalan.flickslate.common.util.runCatchingApi
import com.zsoltbertalan.flickslate.common.util.runCatchingUnit
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
 * For [retrofit2.Response] version see [fetchNetworkFirstResponse]. Response is needed when we want to extract
 * information from the header (like eTags) or the error body.
 *
 * Recovers from network errors if the cache contains any data by emitting null.
 *
 * Fetch the data from local database (if available),
 * emit if it's available,
 * perform a network request (if instructed),
 * and if not null, emit the response after saving it to local database.
 * @param fetchFromLocal - A function to retrieve [DOMAIN] data from local database. Must be nullable!
 * @param makeNetworkRequest - A function to make network request and retrieve [REMOTE] data
 * @param saveResponseData - A function to save network reply coming in [REMOTE] format
 * @return [Outcome]<[DOMAIN]> type
 */
@Suppress("unused")
inline fun <REMOTE, DOMAIN> fetchNetworkFirst(
	crossinline fetchFromLocal: () -> Flow<DOMAIN?>,
	crossinline makeNetworkRequest: suspend () -> REMOTE,
	noinline saveResponseData: suspend (REMOTE) -> Unit = { },
	crossinline mapper: REMOTE.() -> DOMAIN
) = flow<Outcome<DOMAIN>> {

	var localData: DOMAIN? = null

	val result = runCatchingApi {
		makeNetworkRequest()
	}.andThen { dto ->
		runCatchingUnit { saveResponseData(dto) }
		Ok(dto)
	}.map {
		it.mapper()
	}.recoverIf(
		{ _ ->
			localData = fetchFromLocal().first()
			localData != null
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

/**
 * [retrofit2.Response] version of [fetchNetworkFirst]. Response is needed when we want to extract information
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
 * @param makeNetworkRequest - A function to make network request and retrieve [REMOTE] data wrapped in [retrofit2.Response]
 * @param saveResponseData - A function to save network reply coming in [REMOTE] format wrapped in [retrofit2.Response]
 * @return Result<[DOMAIN]> type
 */
@Suppress("unused")
inline fun <REMOTE, DOMAIN> fetchNetworkFirstResponse(
	crossinline fetchFromLocal: () -> Flow<DOMAIN?>,
	crossinline makeNetworkRequest: suspend () -> Response<REMOTE>,
	noinline saveResponseData: suspend (Response<REMOTE>) -> Unit = { },
	crossinline mapper: REMOTE.() -> DOMAIN
) = flow<Outcome<DOMAIN>> {

	var localData: DOMAIN? = null

	val result = runCatchingApi {
		makeNetworkRequest()
	}.andThen { response ->
		if (response.isSuccessful) {
			runCatchingUnit { saveResponseData(response) }
			Ok(response.body())
		} else {
			Err(response.handleCode())
		}
	}.map {
		it?.mapper()
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
