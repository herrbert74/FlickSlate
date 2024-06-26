package com.zsoltbertalan.flickslate.util.getresult

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.data.network.dto.GenreResponse
import com.zsoltbertalan.flickslate.data.network.dto.toGenres
import com.zsoltbertalan.flickslate.domain.model.Failure
import com.zsoltbertalan.flickslate.domain.model.Genre
import com.zsoltbertalan.flickslate.ext.Outcome
import com.zsoltbertalan.flickslate.testhelper.GenreDtoMother
import com.zsoltbertalan.flickslate.testhelper.GenreMother
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class FetchCacheThenNetworkTest {

	@Test
	fun `when cache has data and network has data then emit twice`() = runTest {

		val fetchFromLocal = { flowOf(GenreMother.createGenreList()) }

		val flow = fetchCacheThenNetwork(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = makeNetworkRequest(),
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

		val flow = fetchCacheThenNetwork(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = makeNetworkRequest(),
			mapper = GenreResponse::toGenres
		)

		flow.toList() shouldBe listOf(
			Ok(GenreMother.createGenreList())
		)

	}

	@Test
	fun `when cache has data and network has NO data then emit once`() = runTest {
		val fetchFromLocal = { flowOf(GenreMother.createGenreList()) }

		val flow = fetchCacheThenNetwork(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = failNetworkRequest(),
			mapper = GenreResponse::toGenres
		)

		flow.toList() shouldBe listOf(
			Ok(GenreMother.createGenreList())
		)

	}

	@Test
	fun `when cache has NO data and network has NO data then emit error`() = runTest {

		val fetchFromLocal = { flowOf(null) }

		val flow = fetchCacheThenNetwork(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = failNetworkRequest(),
			mapper = GenreResponse::toGenres
		)

		flow.first() shouldBe Err(Failure.ServerError)

	}

	@Test
	fun `when cache has data and fetch is cancelled then emit only once`() = runTest {

		val fetchFromLocal = { flowOf(GenreMother.createGenreList()) }

		val results = mutableListOf<Outcome<List<Genre>>>()

		val job = launch(backgroundScope.coroutineContext) {

			fetchCacheThenNetwork(
				fetchFromLocal = fetchFromLocal,
				makeNetworkRequest = makeNetworkRequestDelayed(),
				mapper = GenreResponse::toGenres,
			).collect {
				results.add(it)
			}

		}

		delay(500)
		job.cancel()

		results shouldBe listOf(
			Ok(GenreMother.createGenreList()),
		)

	}

}

suspend fun makeNetworkRequestDelayed(): suspend () -> GenreResponse = suspend {
	delay(1000)
	GenreResponse(GenreDtoMother.createGenreDtoList())
}

fun makeNetworkRequest(): suspend () -> GenreResponse = suspend {
	GenreResponse(GenreDtoMother.createGenreDtoList())
}

@Suppress("RedundantSuspendModifier")
suspend fun failNetworkRequest(): () -> GenreResponse = { throw httpException }

val httpException = HttpException(
	Response.error<GenreResponse>(
		404,
		"Network error".toResponseBody("plain/text".toMediaTypeOrNull())
	)
)
