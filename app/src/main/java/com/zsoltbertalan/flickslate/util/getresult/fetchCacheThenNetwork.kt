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
 * Retrofit Response class is used to handle Etags.
 *
 * Various caching strategies are handled by the different functions in this file.
 */

/**
 * Fetch the data from local database (if available),
 * emit if it's available,
 * perform a network request (if instructed),
 * and if not null, emit the response after saving it to local database.
 * @param fetchFromLocal - A function to retrieve data from local database
 * @param shouldMakeNetworkRequest - Whether or not to make network request
 * @param makeNetworkRequest - A function to make network request
 * @param saveResponseData - A function to save network response
 * @return Result<[DOMAIN]> type
 */
@Suppress("RemoveExplicitTypeArguments")
inline fun <REMOTE, DOMAIN> fetchCacheThenNetwork(
	crossinline fetchFromLocal: () -> Flow<DOMAIN>,
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
			{ throwable -> (throwable as? HttpException)?.code() == HTTP_NOT_MODIFIED },
			{ null }
		).mapError {
			//TODO map it later
			it
		}
		if (result is Err || (result is Ok && result.component1() != null)) {
			emitAll(
				flowOf(
					result.map { it!! }
				)
			)
		}
	} else {
		emitAll(
			flowOf(Ok(localData))
		)
	}
}