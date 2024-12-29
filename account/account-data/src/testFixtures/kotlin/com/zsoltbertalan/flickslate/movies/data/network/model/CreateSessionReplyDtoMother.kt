package com.zsoltbertalan.flickslate.movies.data.network.model

import com.zsoltbertalan.flickslate.account.data.network.model.CreateSessionReplyDto

object CreateSessionReplyDtoMother {

	fun createCreateSessionReplyDto(success: Boolean = true) = CreateSessionReplyDto(
		success = success,
		session_id = "session123abc"
	)

}
