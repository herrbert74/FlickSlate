package com.zsoltbertalan.flickslate.account.data.network.model

internal object DeleteSessionReplyDtoMother {

	fun createDeleteSessionReplyDto(success: Boolean = true) = DeleteSessionReplyDto(
		success = success,
	)

}
