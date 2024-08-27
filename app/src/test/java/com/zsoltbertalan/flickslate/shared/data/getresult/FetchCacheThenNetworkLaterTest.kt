package com.zsoltbertalan.flickslate.shared.data.getresult

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.search.data.network.model.GenreReplyDto
import com.zsoltbertalan.flickslate.search.data.network.model.toGenres
import com.zsoltbertalan.flickslate.shared.domain.model.Failure
import com.zsoltbertalan.flickslate.shared.domain.model.Genre
import com.zsoltbertalan.flickslate.shared.util.Outcome
import com.zsoltbertalan.flickslate.shared.testhelper.GenreMother
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test

class FetchCacheThenNetworkLaterTest {

	@Test
	fun `when cache has data and network has data then emit once`() = runTest {

		val fetchFromLocal = { flowOf(GenreMother.createGenreList()) }

		val flow = fetchCacheThenNetwork(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = makeNetworkRequest(),
			mapper = GenreReplyDto::toGenres,
			strategy = STRATEGY.CACHE_FIRST_NETWORK_LATER
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
			mapper = GenreReplyDto::toGenres,
			strategy = STRATEGY.CACHE_FIRST_NETWORK_LATER
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
			mapper = GenreReplyDto::toGenres,
			strategy = STRATEGY.CACHE_FIRST_NETWORK_LATER
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
			mapper = GenreReplyDto::toGenres,
			strategy = STRATEGY.CACHE_FIRST_NETWORK_LATER
		)

		flow.first() shouldBe Err(Failure.ServerError("Invalid id: The pre-requisite id is invalid or not found."))

	}

	@Test
	fun `when cache has data and fetch is cancelled then emit only once`() = runTest {

		val fetchFromLocal = { flowOf(GenreMother.createGenreList()) }

		val results = mutableListOf<Outcome<List<Genre>>>()

		val job = launch(backgroundScope.coroutineContext) {

			fetchCacheThenNetwork(
				fetchFromLocal = fetchFromLocal,
				makeNetworkRequest = makeNetworkRequestDelayed(),
				mapper = GenreReplyDto::toGenres,
				strategy = STRATEGY.CACHE_FIRST_NETWORK_LATER
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
