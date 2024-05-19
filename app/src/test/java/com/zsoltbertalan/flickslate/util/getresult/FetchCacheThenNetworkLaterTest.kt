package com.zsoltbertalan.flickslate.util.getresult

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.data.network.dto.GenreResponse
import com.zsoltbertalan.flickslate.data.network.dto.toGenres
import com.zsoltbertalan.flickslate.testhelper.GenreMother
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test

class FetchCacheThenNetworkLaterTest {

	@Test
	fun `when cache has data and network has data then emit once`() = runTest {

		val fetchFromLocal = { flowOf(GenreMother.createGenreList()) }

		val flow = fetchCacheThenNetwork(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = makeNetworkRequest(),
			mapper = GenreResponse::toGenres,
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
			mapper = GenreResponse::toGenres,
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
			mapper = GenreResponse::toGenres,
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
			mapper = GenreResponse::toGenres,
			strategy = STRATEGY.CACHE_FIRST_NETWORK_LATER
		)

		flow.first() shouldBe Err(httpException)

	}

}