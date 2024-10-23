package com.zsoltbertalan.flickslate.shared.data.getresult

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.search.data.network.model.GenreReplyDto
import com.zsoltbertalan.flickslate.search.data.network.model.toGenres
import com.zsoltbertalan.flickslate.shared.domain.model.Failure
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.testhelper.GenreMother
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test

class FetchCacheThenRemoteTest {

	@Test
	fun `when cache has data and network has data then emit twice`() = runTest {

		val fetchFromLocal = { flowOf(PagingReply(GenreMother.createGenreList(), false, PageData())) }

		val flow = fetchCacheThenRemote(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = { makeNetworkRequestResult()() }
		)

		flow.toList().size shouldBe 2
		flow.toList().first().value.pagingList shouldBe GenreMother.createGenreList()
		flow.toList().last().value.pagingList shouldBe GenreMother.createGenreList()

	}

	@Test
	fun `when cache has NO data and network has data then emit once`() = runTest {

		val fetchFromLocal = { flowOf(null) }

		val flow = fetchCacheThenRemote(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = { makeNetworkRequestResult()() }
		)

		flow.toList().first().value.pagingList shouldBe GenreMother.createGenreList()

	}

	@Test
	fun `when cache has data and network has NO data then emit once`() = runTest {
		val fetchFromLocal = { flowOf(GenreMother.createGenreList()) }

		val flow = fetchCacheThenRemote(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = { failNetworkRequestWithResultServerError()() }
		)

		flow.toList() shouldBe listOf(
			Ok(GenreMother.createGenreList())
		)

	}

	@Test
	fun `when cache has data and network returns NOT_MODIFIED then emit once`() = runTest {
		val fetchFromLocal = { flowOf(GenreMother.createGenreList()) }

		val flow = fetchCacheThenRemote(
			fetchFromLocal = fetchFromLocal,
			makeNetworkRequest = { failNetworkRequestWithResultNotModified()() }
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
