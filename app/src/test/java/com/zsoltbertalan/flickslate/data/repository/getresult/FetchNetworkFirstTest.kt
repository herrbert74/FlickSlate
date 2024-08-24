package com.zsoltbertalan.flickslate.data.repository.getresult

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.data.network.dto.GenreReplyDto
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

class FetchNetworkFirstTest {

	@Test
	fun `when network has data then emit once`() = runTest {

		val fetchFromLocal = { flowOf(GenreMother.createGenreList()) }

		val flow = fetchNetworkFirst(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = makeNetworkRequest(),
			mapper = GenreReplyDto::toGenres,
		)

		flow.toList() shouldBe listOf(
			Ok(GenreMother.createGenreList()),
		)

	}

	@Test
	fun `when network has NO data and cache has data then emit once`() = runTest {
		val fetchFromLocal = { flowOf(GenreMother.createGenreList()) }

		val flow = fetchNetworkFirst(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = failNetworkRequest(),
			mapper = GenreReplyDto::toGenres
		)

		flow.toList() shouldBe listOf(
			Ok(GenreMother.createGenreList())
		)

	}


	@Test
	fun `when network has NO data and cache has NO data then emit error`() = runTest {

		val fetchFromLocal = { flowOf(null) }

		val flow = fetchNetworkFirst(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = failNetworkRequest(),
			mapper = GenreReplyDto::toGenres
		)

		flow.first() shouldBe Err(Failure.ServerError("Invalid id: The pre-requisite id is invalid or not found."))

	}

	@Test
	fun `when fetch is cancelled then do not emit`() = runTest {

		val fetchFromLocal = { flowOf(GenreMother.createGenreList()) }

		val results = mutableListOf<Outcome<List<Genre>>>()

		val job = launch(backgroundScope.coroutineContext) {

			fetchNetworkFirst(
				fetchFromLocal = fetchFromLocal,
				makeNetworkRequest = makeNetworkRequestDelayed(),
				mapper = GenreReplyDto::toGenres,
			).collect {
				results.add(it)
			}

		}

		delay(500)
		job.cancel()

		results shouldBe emptyList()

	}

}
