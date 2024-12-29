package com.zsoltbertalan.flickslate.account.data.network.model

import com.zsoltbertalan.flickslate.movies.data.network.model.AccountReplyDtoMother
import com.zsoltbertalan.flickslate.shared.model.Account
import com.zsoltbertalan.flickslate.shared.model.Movie
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import io.kotest.matchers.shouldBe
import org.junit.Before
import org.junit.Test

class AccountDetailsReplyDtoTest {

	@Test
	fun `when there is a name in response then name is mapped`() {
		val responseDto = AccountReplyDtoMother.createAccountReplyDto()
		val mappedResponse = responseDto.toAccount()
		mappedResponse.name shouldBe "John Doe"
	}

	@Test
	fun `when there is no name in response then username is mapped`() {
		val responseDto = AccountReplyDtoMother.createAccountReplyDtoWithoutName()
		val mappedResponse = responseDto.toAccount()
		mappedResponse.name shouldBe "Jane Doe"
	}

}
