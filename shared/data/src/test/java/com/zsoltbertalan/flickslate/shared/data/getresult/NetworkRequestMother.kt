package com.zsoltbertalan.flickslate.shared.data.getresult

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.shared.domain.model.Genre
import com.zsoltbertalan.flickslate.shared.domain.model.GenreMother
import com.zsoltbertalan.flickslate.base.kotlin.result.Failure
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import kotlinx.coroutines.delay

fun makeNetworkRequestResult(): () -> Outcome<List<Genre>> =
	{ Ok(GenreMother.createGenreList()) }

fun failNetworkRequestWithResultServerError(): () -> Outcome<List<Genre>> =
	{ Err(Failure.ServerError("Invalid id: The pre-requisite id is invalid or not found.")) }

fun failNetworkRequestWithResultNotModified(): () -> Outcome<List<Genre>> =
	{ Err(Failure.NotModified) }

fun makeNetworkRequestDelayedResponse(): suspend () -> Outcome<List<Genre>> = suspend {
	delay(1000)
	Ok(GenreMother.createGenreList())
}

object NetworkRequestMother {

	private var attempt = 0

	fun resetAttempts() {
		attempt = 0
	}

	fun makeNetworkRequestResultAfterRetry(): suspend () -> Outcome<List<Genre>> = suspend {
		if (attempt == 0) {
			attempt++
			Err(Failure.ServerError("Invalid id: The pre-requisite id is invalid or not found."))
		} else {
			Ok(GenreMother.createGenreList())
		}
	}
}
