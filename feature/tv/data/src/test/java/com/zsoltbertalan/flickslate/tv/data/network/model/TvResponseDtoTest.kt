package com.zsoltbertalan.flickslate.tv.data.network.model

import com.zsoltbertalan.flickslate.shared.data.network.successResponseCall
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.tv.data.network.TvService
import com.zsoltbertalan.flickslate.tv.domain.model.TvShow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import mockwebserver3.MockWebServer
import okhttp3.MediaType.Companion.toMediaType
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class TvResponseDtoTest {

	private val server = MockWebServer()
	private lateinit var api: TvService
	private lateinit var mappedResponse: PagingReply<TvShow>

	@Before
	fun before() {
		val json = Json { ignoreUnknownKeys = true }
		val jsonConverterFactory = json.asConverterFactory("application/json; charset=UTF8".toMediaType())
		server.start()
		api = Retrofit.Builder()
			.baseUrl(server.url("/"))
			.addConverterFactory(jsonConverterFactory)
			.build().create(TvService::class.java)

		val replyDto = TvDtoMother.createTopRatedTvReplyDto()
		runBlocking {
			val response = successResponseCall(server, replyDto) { api.getTopRatedTv(page = 1) }

			mappedResponse = response.toTvShowsReply()
		}

	}

	@After
	fun after() {
		server.close()
	}

	@Test
	fun `when there is a response then name is mapped`() {
		mappedResponse.pagingList[0].name shouldBe "Detectorists"
	}

	@Test
	fun `when there is a response then overview is mapped`() {
		mappedResponse.pagingList[3].overview shouldBe "Overview 3"
	}

	@Test
	fun `when there is a response then posterPath is mapped`() {
		mappedResponse.pagingList[0].posterPath shouldBe "/eclnU0b9BbvykXoXEd3CGAFwJUO.jpg"
	}

}
