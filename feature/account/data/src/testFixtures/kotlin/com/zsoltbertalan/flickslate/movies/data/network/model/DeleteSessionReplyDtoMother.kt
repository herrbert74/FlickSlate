package com.zsoltbertalan.flickslate.movies.data.network.model

import com.zsoltbertalan.flickslate.account.data.network.model.DeleteSessionReplyDto

object DeleteSessionReplyDtoMother {

	fun createDeleteSessionReplyDto(success: Boolean = true) = DeleteSessionReplyDto(
		success = success,
	)

}
