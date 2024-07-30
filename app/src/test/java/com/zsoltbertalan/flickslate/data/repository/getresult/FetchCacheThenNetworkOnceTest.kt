package com.zsoltbertalan.flickslate.data.repository.getresult

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.data.network.dto.GenreResponse
import com.zsoltbertalan.flickslate.data.network.dto.toGenres
import com.zsoltbertalan.flickslate.domain.model.Failure
import com.zsoltbertalan.flickslate.domain.model.Genre
import com.zsoltbertalan.flickslate.common.util.Outcome
import com.zsoltbertalan.flickslate.common.testhelper.GenreMother
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test

class FetchCacheThenNetworkOnceTest {

	@Test
	fun `when cache has data and network has data then emit once`() = runTest {

		val fetchFromLocal = { flowOf(GenreMother.createGenreList()) }

		val flow = fetchCacheThenNetwork(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = makeNetworkRequest(),
			mapper = GenreResponse::toGenres,
			strategy = STRATEGY.CACHE_FIRST_NETWORK_ONCE
		)

		flow.toList() shouldBe listOf(
			Ok(GenreMother.createGenreList()),
		)

	}

	@Test
	fun `when cache has NO data and network has data then emit once`() = runTest {

		val fetchFromLocal = { flowOf(null) }

		val flow = fetchCacheThenNetwork(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = makeNetworkRequest(),
			mapper = GenreResponse::toGenres,
			strategy = STRATEGY.CACHE_FIRST_NETWORK_ONCE
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
			mapper = GenreResponse::toGenres,
			strategy = STRATEGY.CACHE_FIRST_NETWORK_ONCE
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
			mapper = GenreResponse::toGenres,
			strategy = STRATEGY.CACHE_FIRST_NETWORK_ONCE
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
				strategy = STRATEGY.CACHE_FIRST_NETWORK_ONCE
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
