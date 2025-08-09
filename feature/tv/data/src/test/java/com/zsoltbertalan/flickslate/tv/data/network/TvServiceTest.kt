package com.zsoltbertalan.flickslate.tv.data.network

import com.zsoltbertalan.flickslate.shared.data.network.failureResponseCall
import com.zsoltbertalan.flickslate.shared.data.network.successResponseCall
import com.zsoltbertalan.flickslate.tv.data.network.model.TvDtoMother
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import mockwebserver3.MockWebServer
import okhttp3.MediaType.Companion.toMediaType
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

/**
 * This is strictly testing the network layer. Still not sure if this is the intended usage for MockWebServer or
 * integration tests, where it could replace mocks. Maybe testing various response codes? Or conversion? But we use
 * data source for that!
 */
class TvServiceTest {

	private val server = MockWebServer()
	private lateinit var api: TvService

	@Before
	fun before() {
		val json = Json { ignoreUnknownKeys = true }
		val jsonConverterFactory = json.asConverterFactory("application/json; charset=UTF8".toMediaType())
		server.start()
		api = Retrofit.Builder()
			.baseUrl(server.url("/"))
			.addConverterFactory(jsonConverterFactory)
			.build().create(TvService::class.java)
	}

	@After
	fun after() {
		server.close()
	}

	@Test
	fun `getTopRatedTv returns success`() = runTest {
		val dto = TvDtoMother.createTopRatedTvReplyDto()
		val topRatedTvResponse = successResponseCall(server, dto) { api.getTopRatedTv(page = 1) }

		topRatedTvResponse.body() shouldBe dto
	}

	@Test
	fun `getTopRatedTv returns error`() = runTest {
		val topRatedTvResponse = failureResponseCall(server) { api.getTopRatedTv(page = 1) }

		topRatedTvResponse.message() shouldBe "Client Error"
		topRatedTvResponse.errorBody()?.string() shouldBe "message = \"Client error\""
	}

}
