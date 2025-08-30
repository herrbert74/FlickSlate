package com.zsoltbertalan.flickslate.movies.data.network.model

import com.zsoltbertalan.flickslate.account.data.network.model.CreateRequestTokenReplyDto

internal object CreateRequestTokenReplyDtoMother {

	fun createCreateRequestTokenReplyDto(success: Boolean = true) = CreateRequestTokenReplyDto(
		success = success,
		expires_at = "",
		request_token = "request123abc"
	)

}
