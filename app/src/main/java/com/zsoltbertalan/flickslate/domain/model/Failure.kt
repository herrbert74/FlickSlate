package com.zsoltbertalan.flickslate.domain.model

sealed class Failure {

	data class ServerError(val message: String) : Failure()
	data object UnknownApiError : Failure()
	data object IoFailure : Failure()
	data object UnknownHostFailure : Failure()
	data object UnexpectedFailure : Failure()
	data object NotModified : Failure()

}
