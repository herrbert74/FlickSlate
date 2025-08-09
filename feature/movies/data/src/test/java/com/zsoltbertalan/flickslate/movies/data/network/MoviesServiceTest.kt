package com.zsoltbertalan.flickslate.movies.data.network

import com.zsoltbertalan.flickslate.movies.data.network.model.MovieDtoMother
import com.zsoltbertalan.flickslate.shared.data.network.failureResponseCall
import com.zsoltbertalan.flickslate.shared.data.network.successResponseCall
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
class MoviesServiceTest {

	private val server = MockWebServer()
	private lateinit var api: MoviesService

	@Before
	fun before() {
		val json = Json { ignoreUnknownKeys = true }
		val jsonConverterFactory = json.asConverterFactory("application/json; charset=UTF8".toMediaType())
		server.start()
		api = Retrofit.Builder()
			.baseUrl(server.url("/"))
			.addConverterFactory(jsonConverterFactory)
			.build().create(MoviesService::class.java)
	}

	@After
	fun after() {
		server.close()
	}

	@Test
	fun `getPopularMovies returns success`() = runTest {
		val dto = MovieDtoMother.createPopularMovieList()
		val data = successResponseCall(server, dto) { api.getPopularMovies(page = 1) }

		data.body() shouldBe dto
	}

	@Test
	fun `getPopularMovies returns error`() = runTest {
		val data = failureResponseCall(server) { api.getPopularMovies(page = 1) }

		data.message() shouldBe "Client Error"
		data.errorBody()?.string() shouldBe "message = \"Client error\""
	}

	@Test
	fun `getNowPlayingMovies returns success`() = runTest {
		val dto = MovieDtoMother.createNowPlayingMovieList()
		val data = successResponseCall(server, dto) { api.getNowPlayingMovies(page = 1) }

		data.body() shouldBe dto
	}

	@Test
	fun `getNowPlayingMovies returns error`() = runTest {
		val data = failureResponseCall(server) { api.getNowPlayingMovies(page = 1) }

		data.message() shouldBe "Client Error"
		data.errorBody()?.string() shouldBe "message = \"Client error\""
	}

	@Test
	fun `getUpcomingMovies returns success`() = runTest {
		val dto = MovieDtoMother.createUpcomingMovieList()
		val data = successResponseCall(server, dto) { api.getUpcomingMovies(page = 1) }

		data.body() shouldBe dto
	}

	@Test
	fun `getUpcomingMovies returns error`() = runTest {
		val data = failureResponseCall(server) { api.getUpcomingMovies(page = 1) }

		data.message() shouldBe "Client Error"
		data.errorBody()?.string() shouldBe "message = \"Client error\""
	}

}
