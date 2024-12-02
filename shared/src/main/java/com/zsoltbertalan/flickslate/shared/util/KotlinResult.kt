package com.zsoltbertalan.flickslate.shared.util

import com.github.michaelbull.result.Result
import com.zsoltbertalan.flickslate.shared.model.Failure

/**
 * A [Result] that maps [com.github.michaelbull.result.Err] to [Failure].
 */
typealias Outcome<T> = Result<T, Failure>
