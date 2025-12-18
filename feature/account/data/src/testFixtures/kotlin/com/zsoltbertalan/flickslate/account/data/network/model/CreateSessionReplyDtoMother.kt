package com.zsoltbertalan.flickslate.account.data.network.model

internal object CreateSessionReplyDtoMother {

	fun createCreateSessionReplyDto(success: Boolean = true) = CreateSessionReplyDto(
		success = success,
		session_id = "session123abc"
	)

}
