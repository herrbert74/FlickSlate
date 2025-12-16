package com.zsoltbertalan.flickslate.account.data.network

import com.github.michaelbull.result.Err
import com.zsoltbertalan.flickslate.account.data.network.model.CreateRequestTokenReplyDto
import com.zsoltbertalan.flickslate.account.data.network.model.CreateSessionReplyDto
import com.zsoltbertalan.flickslate.account.data.network.model.CreateRequestTokenReplyDtoMother
import com.zsoltbertalan.flickslate.account.data.network.model.CreateSessionReplyDtoMother
import com.zsoltbertalan.flickslate.shared.data.network.model.ErrorBody
import com.zsoltbertalan.flickslate.shared.kotlin.result.Failure
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.HttpURLConnection

class AccountRemoteDataSourceTest {

	private val accountService: AccountService = mockk()
	private lateinit var accountRemoteDataSource: AccountRemoteDataSource

	@Before
	fun setup() {
		coEvery { accountService.createRequestToken() } returns
			CreateRequestTokenReplyDtoMother.createCreateRequestTokenReplyDto()
		coEvery { accountService.validateRequestTokenWithLogin(any()) } returns
			CreateRequestTokenReplyDtoMother.createCreateRequestTokenReplyDto()
		coEvery { accountService.createSession(any()) } returns
			CreateSessionReplyDtoMother.createCreateSessionReplyDto()
		accountRemoteDataSource = AccountRemoteDataSource(accountService)
	}

	@Test
	fun `when createSessionId called and service returns result then returns correct result`() = runTest {
		val result = accountRemoteDataSource.createSessionId("", "")
		result.value shouldBeEqual "session123abc"
	}

	@Test
	fun `when createSessionId called and service returns create request failure then returns correct result`() =
		runTest {
			coEvery { accountService.createRequestToken() } throws createRequestTokenException()
			accountRemoteDataSource.createSessionId("", "") shouldBeEqual
				Err(Failure.ServerError("Invalid service: this service does not exist."))
		}

	@Test
	fun `when createSessionId called and service returns create 2 failure then returns correct result`() =
		runTest {
			coEvery { accountService.validateRequestTokenWithLogin(any()) } throws createRequestTokenException()
			accountRemoteDataSource.createSessionId("", "") shouldBeEqual
				Err(Failure.ServerError("Invalid service: this service does not exist."))
		}

	@Test
	fun `when createSessionId called and service returns create 3 failure then returns correct result`() =
		runTest {
			coEvery { accountService.createSession(any()) } throws createSessionException()
			accountRemoteDataSource.createSessionId("", "") shouldBeEqual
				Err(Failure.ServerError("Invalid service: this service does not exist."))
		}

	@Suppress("unused")
	private fun createRequestTokenException(): HttpException {
		val errorBody = ErrorBody(
			success = false,
			status_code = 2,
			status_message = "Invalid service: this service does not exist."
		)
		return HttpException(
			Response.error<CreateRequestTokenReplyDto>(
				HttpURLConnection.HTTP_NOT_IMPLEMENTED,
				Json.encodeToString(errorBody).toResponseBody()
			)
		)
	}

	@Suppress("unused")
	private fun createSessionException(): HttpException {
		val errorBody = ErrorBody(
			success = false,
			status_code = 2,
			status_message = "Invalid service: this service does not exist."
		)
		return HttpException(
			Response.error<CreateSessionReplyDto>(
				HttpURLConnection.HTTP_NOT_IMPLEMENTED,
				Json.encodeToString(errorBody).toResponseBody()
			)
		)
	}

}
