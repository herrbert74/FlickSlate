package com.zsoltbertalan.flickslate.data.repository.getresult

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import com.zsoltbertalan.flickslate.common.util.Outcome
import com.zsoltbertalan.flickslate.common.util.runCatchingApi
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
 * For [retrofit2.Response] version see [fetchNetworkOnlyResponse]. Response is needed when we want to extract
 * information from the header (like eTags) or the error body.
 *
 * @param makeNetworkRequest - A function to make network request and retrieve [REMOTE] data
 * @return Result<[DOMAIN]> type
 */
@Suppress("unused")
suspend inline fun <REMOTE, DOMAIN> fetchNetworkOnly(
	crossinline makeNetworkRequest: suspend () -> REMOTE,
	crossinline mapper: REMOTE.() -> DOMAIN
): Outcome<DOMAIN> {

	return runCatchingApi {
		makeNetworkRequest()
	}.map {
		it.mapper()
	}

}

/**
 * [retrofit2.Response] version of [fetchNetworkOnly]. Response is needed when we want to extract information
 * from the header (like eTags) or the error body. Otherwise use the simpler version above.
 *
 * @param makeNetworkRequest - A function to make network request and retrieve [REMOTE] data wrapped in [retrofit2.Response]
 * @return Result<[DOMAIN]> type
 */
@Suppress("unused")
suspend inline fun <REMOTE, DOMAIN> fetchNetworkOnlyResponse(
	crossinline makeNetworkRequest: suspend () -> Response<REMOTE>,
	crossinline mapper: REMOTE.() -> DOMAIN
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
