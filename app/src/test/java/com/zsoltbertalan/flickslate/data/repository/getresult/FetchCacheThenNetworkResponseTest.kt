package com.zsoltbertalan.flickslate.data.repository.getresult

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.data.network.dto.GenreReplyDto
import com.zsoltbertalan.flickslate.data.network.dto.toGenres
import com.zsoltbertalan.flickslate.domain.model.Failure
import com.zsoltbertalan.flickslate.common.testhelper.GenreMother
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test

class FetchCacheThenNetworkResponseTest {

	@Test
	fun `when cache has data and network has data then emit twice`() = runTest {

		val fetchFromLocal = { flowOf(GenreMother.createGenreList()) }

		val flow = fetchCacheThenNetworkResponse(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = makeNetworkRequestResponse(),
			mapper = GenreReplyDto::toGenres,
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
			mapper = GenreReplyDto::toGenres
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
			makeNetworkRequest = failNetworkRequestWithResponse(),
			mapper = GenreReplyDto::toGenres
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
			makeNetworkRequest = failNetworkRequestWithResponse(),
			mapper = GenreReplyDto::toGenres
		)

		flow.first() shouldBe Err(Failure.ServerError("Invalid id: The pre-requisite id is invalid or not found."))

	}

}
