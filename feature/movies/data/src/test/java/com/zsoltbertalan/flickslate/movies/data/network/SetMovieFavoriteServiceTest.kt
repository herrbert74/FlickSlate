package com.zsoltbertalan.flickslate.movies.data.network

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class SetMovieFavoriteServiceTest {

	private val server = MockWebServer()
	private lateinit var api: SetMovieFavoriteService

	@Before
	fun before() {
		val json = Json { ignoreUnknownKeys = true }
		val jsonConverterFactory = json.asConverterFactory("application/json; charset=UTF8".toMediaType())
		server.start()
		api = Retrofit.Builder()
			.baseUrl(server.url("/"))
			.addConverterFactory(jsonConverterFactory)
			.build().create(SetMovieFavoriteService::class.java)
	}

	@After
	fun after() {
		server.close()
	}

	@Test
	fun `setMovieFavorite sends correct request`() = runTest {
		server.enqueue(MockResponse(code = 200, body = "{}"))

		val bodyJson = buildJsonObject {
			put("media_type", "movie")
			put("media_id", 10)
			put("favorite", true)
		}
		val body = bodyJson.toString().toRequestBody("application/json; charset=UTF-8".toMediaType())

		api.setMovieFavorite(accountId = 1, sessionId = "session", body = body)

		val request = server.takeRequest()
		request.method shouldBe "POST"
		request.requestLine shouldBe "POST /account/1/favorite?session_id=session HTTP/1.1"

		val sentBody = request.body?.utf8() ?: error("Missing request body")
		val sentJson = Json.parseToJsonElement(sentBody)
		val expectedJson = Json.parseToJsonElement(bodyJson.toString())
		sentJson shouldBe expectedJson
	}

}
