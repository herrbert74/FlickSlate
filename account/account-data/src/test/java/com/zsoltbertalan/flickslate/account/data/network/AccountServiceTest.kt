package com.zsoltbertalan.flickslate.account.data.network

import com.zsoltbertalan.flickslate.movies.data.network.model.AccountReplyDtoMother
import com.zsoltbertalan.flickslate.shared.data.network.failureCall
import com.zsoltbertalan.flickslate.shared.data.network.successCall
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import mockwebserver3.MockWebServer
import okhttp3.MediaType.Companion.toMediaType
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

/**
 * This is strictly testing the network layer. Still not sure if this is the intended usage for MockWebServer or
 * integration tests, where it could replace mocks. Maybe testing various response codes? Or conversion? But we use
 * data source for that!
 */
@RunWith(RobolectricTestRunner::class)
class AccountServiceTest {

	private val server = MockWebServer()
	private lateinit var api: AccountService

	@Before
	fun before() {
		val json = Json { ignoreUnknownKeys = true }
		val jsonConverterFactory = json.asConverterFactory("application/json; charset=UTF8".toMediaType())
		server.start()
		api = Retrofit.Builder()
			.baseUrl(server.url("/"))
			.addConverterFactory(jsonConverterFactory)
			.build().create(AccountService::class.java)
	}

	@After
	fun after() {
		server.close()
	}

	@Test
	fun `getAccountDetails returns success`() = runTest {
		val dto = AccountReplyDtoMother.createAccountReplyDto()
		val data = successCall(server, dto) { api.getAccountDetails("") }

		data shouldBe dto
	}

	@Test
	fun `getAccountDetails returns error`(): Unit = runTest {
		try {
			failureCall(server) { api.getAccountDetails("") }
		} catch (e: HttpException) {
			e.code() shouldBe 404
			e.message() shouldBe "Client Error"
		}
	}

}
