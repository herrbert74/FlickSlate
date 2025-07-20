package com.zsoltbertalan.flickslate.shared.data.getresult

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.shared.model.Failure
import com.zsoltbertalan.flickslate.shared.model.Genre
import com.zsoltbertalan.flickslate.shared.model.GenreMother
import com.zsoltbertalan.flickslate.shared.model.PageData
import com.zsoltbertalan.flickslate.shared.model.PagingReply
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
import org.junit.After
import org.junit.Test

class FetchRemoteFirstTest {

	val mockSaveResponseData: (List<Genre>) -> Unit = mockk(relaxed = true)

	@After
	fun after() {
		NetworkRequestMother.resetAttempts()
	}

	@Test
	fun `when network has data and cache has data then emit once`() = runTest {
		val fetchFromLocal = { flowOf(PagingReply(GenreMother.createGenreList(), false, PageData())) }

		val flow = fetchRemoteFirst(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = { makeNetworkRequestResult()() }
		)

		flow.toList() shouldBe listOf(
			Ok(GenreMother.createGenreList()),
		)

	}

	@Test
	fun `when network has data and cache has NO data then emit once`() = runTest {
		val fetchFromLocal = { flowOf(null) }

		val flow = fetchRemoteFirst(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = { makeNetworkRequestResult()() }
		)

		flow.toList() shouldBe listOf(
			Ok(GenreMother.createGenreList())
		)

	}

	@Test
	fun `when network has NO data and cache has data then emit once`() = runTest {
		val fetchFromLocal = { flowOf(GenreMother.createGenreList()) }

		val flow = fetchRemoteFirst(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = { failNetworkRequestWithResultServerError()() }
		)

		flow.toList() shouldBe listOf(
			Ok(GenreMother.createGenreList())
		)

	}

	@Test
	fun `when network returns NOT_MODIFIED and cache has data then emit once`() = runTest {
		val fetchFromLocal = { flowOf(GenreMother.createGenreList()) }

		val flow = fetchRemoteFirst(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = { failNetworkRequestWithResultNotModified()() }
		)

		flow.toList() shouldBe listOf(
			Ok(GenreMother.createGenreList())
		)

	}

	@Test
	fun `when network has NO data and cache has NO data then emit error`() = runTest {
		val fetchFromLocal = { flowOf(null) }
		val flow = fetchRemoteFirst(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = failNetworkRequestWithResultServerError(),
		)

		flow.first() shouldBe Err(Failure.ServerError("Invalid id: The pre-requisite id is invalid or not found."))

	}

	@Test
	fun `when fetch is cancelled then do not emit`() = runTest {
		val fetchFromLocal = { flowOf(GenreMother.createGenreList()) }

		val results = mutableListOf<Outcome<List<Genre>>>()

		val job = launch(backgroundScope.coroutineContext) {
			fetchRemoteFirst(
				fetchFromLocal = fetchFromLocal,
				makeNetworkRequest = makeNetworkRequestDelayedResponse(),
			).collect {
				results.add(it)
			}

		}

		delay(500)
		job.cancel()

		results shouldBe listOf()

	}

	@Test
	fun `given default retry policy when cache has NO data and network has data after retry then emit once`() =
		runTest {
			val fetchFromLocal = { flowOf(null) }

			val flow = fetchRemoteFirst(
				fetchFromLocal = fetchFromLocal,
				makeNetworkRequest = NetworkRequestMother.makeNetworkRequestResultAfterRetry(),
				saveResponseData = mockSaveResponseData,
				retryPolicy = defaultRetryPolicy
			)

			flow.toList() shouldBe listOf(
				Ok(GenreMother.createGenreList())
			)
			verify(exactly = 1) { mockSaveResponseData(any()) }

		}

	@Test
	fun `given default no retry policy is when cache has NO data and network has data after retry then emit error`() =
		runTest {
			val fetchFromLocal = { flowOf(null) }

			val flow = fetchRemoteFirst(
				fetchFromLocal = fetchFromLocal,
				makeNetworkRequest = NetworkRequestMother.makeNetworkRequestResultAfterRetry(),
				saveResponseData = mockSaveResponseData,
			)

			flow.first() shouldBe Err(Failure.ServerError("Invalid id: The pre-requisite id is invalid or not found."))
			verify(exactly = 0) { mockSaveResponseData(any()) }

		}

}
