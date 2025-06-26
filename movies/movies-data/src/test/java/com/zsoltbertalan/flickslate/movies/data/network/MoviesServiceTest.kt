package com.zsoltbertalan.flickslate.movies.data.network

import com.zsoltbertalan.flickslate.movies.data.network.model.MovieDtoMother
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
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

/**
 * This is strictly testing the network layer. Still not sure if this is the intended usage for MockWebServer or
 * integration tests, where it could replace mocks. Maybe testing various response codes? Or conversion? But we use 
 * data source for that!
 */
@RunWith(RobolectricTestRunner::class)
class MoviesServiceTest {

	private val server = MockWebServer()
	private lateinit var api: MoviesService

	@Before
	fun before() {
		val json = Json { ignoreUnknownKeys = true }
		val jsonConverterFactory = json.asConverterFactory("application/json; charset=UTF8".toMediaType())
		api = Retrofit.Builder()
			.baseUrl(server.url("/"))
			.addConverterFactory(jsonConverterFactory)
			.build().create(MoviesService::class.java)
	}

	@After
	fun after() {
		server.shutdown()
	}

	@Test
	fun `getPopularMovies returns success`() = runTest {
		val dto = MovieDtoMother.createPopularMovieList()
		val data = successCall(dto) { api.getPopularMovies(page = 1) }

		data.body() shouldBe dto
	}

	@Test
	fun `getPopularMovies returns error`() = runTest {
		val data = failureCall { api.getPopularMovies(page = 1) }

		data.message() shouldBe "Client Error"
		data.errorBody()?.string() shouldBe "message = \"Client error\""
	}

	@Test
	fun `getNowPlayingMovies returns success`() = runTest {
		val dto = MovieDtoMother.createNowPlayingMovieList()
		val data = successCall(dto) { api.getNowPlayingMovies(page = 1) }

		data.body() shouldBe dto
	}

	@Test
	fun `getNowPlayingMovies returns error`() = runTest {
		val data = failureCall { api.getNowPlayingMovies(page = 1) }

		data.message() shouldBe "Client Error"
		data.errorBody()?.string() shouldBe "message = \"Client error\""
	}

	@Test
	fun `getUpcomingMovies returns success`() = runTest {
		val dto = MovieDtoMother.createUpcomingMovieList()
		val data = successCall(dto) { api.getUpcomingMovies(page = 1) }

		data.body() shouldBe dto
	}

	@Test
	fun `getUpcomingMovies returns error`() = runTest {
		val data = failureCall { api.getUpcomingMovies(page = 1) }

		data.message() shouldBe "Client Error"
		data.errorBody()?.string() shouldBe "message = \"Client error\""
	}

	private inline fun <reified T> successCall(dto: T, apiCall: () -> Response<T>): Response<T> {
		val json = Json { ignoreUnknownKeys = true }
		val mockResponse = MockResponse(body = json.encodeToJsonElement(dto).toString())
		server.enqueue(mockResponse)
		val data = apiCall()
		server.takeRequest()
		return data
	}

	private inline fun <reified T> failureCall(apiCall: () -> Response<T>): Response<T> {
		val mockResponse = MockResponse(code = 404, body = "message = \"Client error\"")
		server.enqueue(mockResponse)
		val data = apiCall()
		server.takeRequest()
		return data
	}

}
