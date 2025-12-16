package com.zsoltbertalan.flickslate.account.data.network.model

internal object AccountReplyDtoMother {

	fun createAccountReplyDto() = AccountDetailsReplyDto(
		name = "John Doe",
		username = "john.doe",
		iso_639_1 = "en",
		iso_3166_1 = "GB",
		include_adult = false,
		id = 1234,
	)

	fun createAccountReplyDtoWithoutName() = AccountDetailsReplyDto(
		name = null,
		username = "john.doe",
		iso_639_1 = "en",
		iso_3166_1 = "GB",
		include_adult = false,
		id = 1234,
	)

}
