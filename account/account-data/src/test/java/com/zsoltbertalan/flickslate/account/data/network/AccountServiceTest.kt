package com.zsoltbertalan.flickslate.account.data.network

import com.zsoltbertalan.flickslate.movies.data.network.model.AccountReplyDtoMother
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.MediaType.Companion.toMediaType
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

/**
 * This is strictly testing the network layer. Still not sure if this is the intended usage for MockWebServer or
 * integration tests, where it could replace mocks. Maybe testing various response codes? Or conversion? But we use
 * data source for that!
 */
class AccountServiceTest {

	private val server = MockWebServer()
	private lateinit var api: AccountService

	@Before
	fun before() {
		val json = Json { ignoreUnknownKeys = true }
		val jsonConverterFactory = json.asConverterFactory("application/json; charset=UTF8".toMediaType())
		api = Retrofit.Builder()
			.baseUrl(server.url("/"))
			.addConverterFactory(jsonConverterFactory)
			.build().create(AccountService::class.java)
	}

	@After
	fun after() {
		server.shutdown()
	}

	@Test
	fun `getAccountDetails returns success`() = runTest {
		val dto = AccountReplyDtoMother.createAccountReplyDto()
		val data = successCall(dto) { api.getAccountDetails("") }

		data shouldBe dto
	}

	@Test
	fun `getAccountDetails returns error`(): Unit = runTest {
		try {
			failureCall { api.getAccountDetails("") }
		} catch (e: HttpException) {
			e.code() shouldBe 404
			e.message() shouldBe "Client Error"
		}
	}

	private inline fun <reified T> successCall(dto: T, apiCall: () -> T): T {
		val json = Json { ignoreUnknownKeys = true }
		val mockResponse = MockResponse(body = json.encodeToJsonElement(dto).toString())
		server.enqueue(mockResponse)
		val data = apiCall()
		server.takeRequest()
		return data
	}

	private inline fun <reified T> failureCall(apiCall: () -> T): T {
		val mockResponse = MockResponse(code = 404, body = "message = \"Client error\"")
		server.enqueue(mockResponse)
		val data = apiCall()
		return data
	}

}
