package com.zsoltbertalan.flickslate.shared.data.getresult

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.shared.domain.model.Failure
import com.zsoltbertalan.flickslate.shared.domain.model.Genre
import com.zsoltbertalan.flickslate.shared.testhelper.GenreMother
import com.zsoltbertalan.flickslate.shared.util.Outcome
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test

class FetchCacheThenRemoteTest {

	val mockSaveResponseData: (List<Genre>) -> Unit = mockk(relaxed = true)

	@Test
	fun `when cache has data and network has data then emit twice`() = runTest {

		val fetchFromLocal = { flowOf(GenreMother.createGenreList()) }

		val flow = fetchCacheThenRemote(
			fetchFromLocal = fetchFromLocal,
			saveResponseData = mockSaveResponseData,
			makeNetworkRequest = { makeNetworkRequestResult()() }
		)

		flow.toList() shouldBe listOf(
			Ok(GenreMother.createGenreList()),
			Ok(GenreMother.createGenreList()),
		)
		verify(exactly = 1) { mockSaveResponseData(any()) }

	}

	@Test
	fun `when cache has NO data and network has data then emit once`() = runTest {

		val fetchFromLocal = { flowOf(null) }

		val flow = fetchCacheThenRemote(
			fetchFromLocal = fetchFromLocal,
			saveResponseData = mockSaveResponseData,
			makeNetworkRequest = { makeNetworkRequestResult()() }
		)

		flow.toList() shouldBe listOf(
			Ok(GenreMother.createGenreList())
		)
		verify(exactly = 1) { mockSaveResponseData(any()) }

	}

	@Test
	fun `when cache has data and network has NO data then emit once`() = runTest {
		val fetchFromLocal = { flowOf(GenreMother.createGenreList()) }

		val flow = fetchCacheThenRemote(
			fetchFromLocal = fetchFromLocal,
			saveResponseData = mockSaveResponseData,
			makeNetworkRequest = { failNetworkRequestWithResultServerError()() }
		)

		flow.toList() shouldBe listOf(
			Ok(GenreMother.createGenreList())
		)
		verify(exactly = 0) { mockSaveResponseData(any()) }

	}

	@Test
	fun `when cache has data and network returns NOT_MODIFIED then emit once`() = runTest {
		val fetchFromLocal = { flowOf(GenreMother.createGenreList()) }

		val flow = fetchCacheThenRemote(
			fetchFromLocal = fetchFromLocal,
			saveResponseData = mockSaveResponseData,
			makeNetworkRequest = { failNetworkRequestWithResultNotModified()() }
		)

		flow.toList() shouldBe listOf(
			Ok(GenreMother.createGenreList())
		)
		verify(exactly = 0) { mockSaveResponseData(any()) }

	}

	@Test
	fun `when cache has NO data and network has NO data then emit error`() = runTest {

		val fetchFromLocal = { flowOf(null) }
		val flow = fetchCacheThenRemote(
			fetchFromLocal = fetchFromLocal,
			saveResponseData = mockSaveResponseData,
			makeNetworkRequest = failNetworkRequestWithResultServerError(),
		)

		flow.first() shouldBe Err(Failure.ServerError("Invalid id: The pre-requisite id is invalid or not found."))
		verify(exactly = 0) { mockSaveResponseData(any()) }

	}


	@Test
	fun `when cache has data and fetch is cancelled then emit only once`() = runTest {

		val fetchFromLocal = { flowOf(GenreMother.createGenreList()) }

		val results = mutableListOf<Outcome<List<Genre>>>()

		val job = launch(backgroundScope.coroutineContext) {

			fetchCacheThenRemote(
				fetchFromLocal = fetchFromLocal,
				saveResponseData = mockSaveResponseData,
				makeNetworkRequest = makeNetworkRequestDelayedResponse(),
			).collect {
				results.add(it)
			}

		}

		delay(500)
		job.cancel()

		results shouldBe listOf(
			Ok(GenreMother.createGenreList()),
		)
		verify(exactly = 0) { mockSaveResponseData(any()) }

	}

}
