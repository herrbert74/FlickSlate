package com.zsoltbertalan.flickslate.ext

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.runCatching
import kotlin.coroutines.cancellation.CancellationException

typealias ApiResult<T> = Result<T, Throwable>

inline fun <T, V> T.apiRunCatching(block: T.() -> V) = runCatching(block)
	.onFailure { if (it is CancellationException) throw it }
