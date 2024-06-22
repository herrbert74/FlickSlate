package com.zsoltbertalan.flickslate.util.getresult

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.data.network.dto.GenreResponse
import com.zsoltbertalan.flickslate.data.network.dto.toGenres
import com.zsoltbertalan.flickslate.domain.model.Failure
import com.zsoltbertalan.flickslate.testhelper.GenreDtoMother
import com.zsoltbertalan.flickslate.testhelper.GenreMother
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response

class FetchCacheThenNetworkResponseTest {

	@Test
	fun `when cache has data and network has data then emit twice`() = runTest {

		val fetchFromLocal = { flowOf(GenreMother.createGenreList()) }

		val flow = fetchCacheThenNetworkResponse(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = makeNetworkRequestResponse(),
			mapper = GenreResponse::toGenres,
		)

		flow.toList() shouldBe listOf(
			Ok(GenreMother.createGenreList()),
			Ok(GenreMother.createGenreList()),
		)

	}

	@Test
	fun `when cache has NO data and network has data then emit once`() = runTest {

		val fetchFromLocal = { flowOf(null) }

		val flow = fetchCacheThenNetworkResponse(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = makeNetworkRequestResponse(),
			mapper = GenreResponse::toGenres
		)

		flow.toList() shouldBe listOf(
			Ok(GenreMother.createGenreList())
		)

	}

	@Test
	fun `when cache has data and network has NO data then emit once`() = runTest {
		val fetchFromLocal = { flowOf(GenreMother.createGenreList()) }

		val flow = fetchCacheThenNetworkResponse(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = failNetworkRequestResponse(),
			mapper = GenreResponse::toGenres
		)

		flow.toList() shouldBe listOf(
			Ok(GenreMother.createGenreList())
		)

	}


	@Test
	fun `when cache has NO data and network has NO data then emit error`() = runTest {

		val fetchFromLocal = { flowOf(null) }

		val flow = fetchCacheThenNetworkResponse(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = failNetworkRequestResponse(),
			mapper = GenreResponse::toGenres
		)

		flow.first() shouldBe Err(Failure.ServerError)

	}

}

@Suppress("RedundantSuspendModifier")
suspend fun makeNetworkRequestResponse(): () -> Response<GenreResponse> =
	{ Response.success(GenreResponse(GenreDtoMother.createGenreDtoList())) }

@Suppress("RedundantSuspendModifier")
suspend fun failNetworkRequestResponse(): () -> Response<GenreResponse> =
	{ Response.error(400, "This is  synthetic error".toResponseBody()) }
