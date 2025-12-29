package com.zsoltbertalan.flickslate.shared.data.util

import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.runCatching
import com.zsoltbertalan.flickslate.shared.data.getresult.handle
import timber.log.Timber

/**
 * [runCatching] version that handles expected Exceptions and rethrows everything else, including
 * CancellationException, @return [com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome].
 */
inline fun <V> runCatchingApi(block: () -> V) = runCatching(block)
	.mapError { it.handle() }

/**
 * [runCatching] version that handles expected Exceptions and rethrows everything else, including Errors and
 * CancellationException, @return [com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome].
 * To be called on a receiver, for example DataSource.runCatchingApi {...}.
 */
inline fun <T, V> T.runCatchingApi(block: T.() -> V) = runCatching(block)
	.mapError {
		it.handle()
	}

/**
 * [runCatching] version that handles all Throwable exceptions, and logs them.
 * Use it to save data for example, when return type is Unit.
 */
inline fun <V> runCatchingUnit(block: () -> V) = runCatching(block)
	.onFailure { Timber.d("zsoltbertalan* runCatchingUnit: ${it.message}") }
