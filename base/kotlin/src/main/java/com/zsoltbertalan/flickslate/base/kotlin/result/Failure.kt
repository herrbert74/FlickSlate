package com.zsoltbertalan.flickslate.base.kotlin.result

sealed class Failure(open var message: String) {

	data class ServerError(override var message: String) : Failure(message)
	data object UnknownApiError : Failure("Unknown error")
	data object IoFailure : Failure("IO failure")
	data object UnknownHostFailure : Failure("Unknown host")
	data object UnexpectedFailure : Failure("Unexpected exception")
	data object NotModified : Failure("Not modified")
	data object UserNotLoggedIn : Failure("User not logged in")

}
