package com.zsoltbertalan.flickslate.account.data.network.model

internal object CreateRequestTokenReplyDtoMother {

	fun createCreateRequestTokenReplyDto(success: Boolean = true) = CreateRequestTokenReplyDto(
		success = success,
		expires_at = "",
		request_token = "request123abc"
	)

}
