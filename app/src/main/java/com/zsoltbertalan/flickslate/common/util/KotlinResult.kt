package com.zsoltbertalan.flickslate.common.util

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.runCatching
import com.zsoltbertalan.flickslate.domain.model.Failure
import com.zsoltbertalan.flickslate.data.repository.getresult.handle
import timber.log.Timber

/**
 * A [Result] that maps [com.github.michaelbull.result.Err] to [Failure].
 */
typealias Outcome<T> = Result<T, Failure>

/**
 * [runCatching] version that handles expected Exceptions and rethrows everything else, including
 * CancellationException, @return [Outcome].
 */
inline fun <V> runCatchingApi(block: () -> V) = runCatching(block)
	.mapError { it.handle() }

/**
 * [runCatching] version that handles expected Exceptions and rethrows everything else, including Errors and
 * CancellationException, @return [Outcome].
 * To be called on a receiver, for example DataSource.runCatchingApi {...}.
 */
inline fun <T, V> T.runCatchingApi(block: T.() -> V) = runCatching(block)
	.mapError {
		it.handle()
	}

/**
 * [runCatching] version that handles all Throwables, and logs them.
 * Use it to save data for example, when return type is Unit.
 */
inline fun <V> runCatchingUnit(block: () -> V) = runCatching(block)
	.onFailure { Timber.d("zsoltbertalan* runCatchingUnit: ${it.message}") }
