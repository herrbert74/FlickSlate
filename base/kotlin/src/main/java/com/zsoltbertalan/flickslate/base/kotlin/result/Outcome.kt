package com.zsoltbertalan.flickslate.base.kotlin.result

import com.github.michaelbull.result.Result

/**
 * A [Result] that maps [com.github.michaelbull.result.Err] to [Failure].
 */
typealias Outcome<T> = Result<T, Failure>
