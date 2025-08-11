package com.zsoltbertalan.flickslate.shared.kotlin.result

import com.github.michaelbull.result.Result

/**
 * A [com.github.michaelbull.result.Result] that maps [com.github.michaelbull.result.Err] to [Failure].
 */
typealias Outcome<T> = Result<T, Failure>
