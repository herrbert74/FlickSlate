package com.zsoltbertalan.flickslate.account.data.network

import com.zsoltbertalan.flickslate.account.data.network.model.FavoriteMovieReplyDto
import com.zsoltbertalan.flickslate.account.data.network.model.FavoriteTvShowReplyDto
import com.zsoltbertalan.flickslate.account.data.network.model.FavoriteMovieReplyDtoMother
import com.zsoltbertalan.flickslate.account.data.network.model.FavoriteTvShowReplyDtoMother
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
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@RunWith(RobolectricTestRunner::class)
class FavoritesServiceTest {

	private val server = MockWebServer()
	private lateinit var api: FavoritesService

	@Before
	fun before() {
		val json = Json { ignoreUnknownKeys = true }
		val jsonConverterFactory = json.asConverterFactory("application/json; charset=UTF8".toMediaType())
		server.start()
		api = Retrofit.Builder()
			.baseUrl(server.url("/"))
			.addConverterFactory(jsonConverterFactory)
			.build().create(FavoritesService::class.java)
	}

	@After
	fun after() {
		server.close()
	}

	@Test
	fun `getFavoriteMovies returns success`() = runTest {
		val dto = FavoriteMovieReplyDtoMother.createFavoriteMovieReplyDto()
		val data = successResponseCall(server, dto) { api.getFavoriteMovies(accountId = 1, sessionId = "session", page = 1) }

		data.body() shouldBe dto
	}

	@Test
	fun `getFavoriteMovies returns error`() = runTest {
		val data = failureResponseCall(server) { api.getFavoriteMovies(accountId = 1, sessionId = "session", page = 1) }

		data.message() shouldBe "Client Error"
		data.errorBody()?.string() shouldBe "message = \"Client error\""
	}

	@Test
	fun `getFavoriteTvShows returns success`() = runTest {
		val dto = FavoriteTvShowReplyDtoMother.createFavoriteTvShowReplyDto()
		val data = successResponseCall(server, dto) { api.getFavoriteTvShows(accountId = 1, sessionId = "session", page = 1) }

		data.body() shouldBe dto
	}

	@Test
	fun `getFavoriteTvShows returns error`() = runTest {
		val data = failureResponseCall(server) { api.getFavoriteTvShows(accountId = 1, sessionId = "session", page = 1) }

		data.message() shouldBe "Client Error"
		data.errorBody()?.string() shouldBe "message = \"Client error\""
	}

}
