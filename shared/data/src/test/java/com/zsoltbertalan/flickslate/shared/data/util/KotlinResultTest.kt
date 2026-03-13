package com.zsoltbertalan.flickslate.shared.data.util

import com.github.michaelbull.result.Err
import com.zsoltbertalan.flickslate.base.kotlin.result.Failure
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CancellationException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException
import kotlin.test.Test

class KotlinResultTest {

	private data class Receiver(val value: Int)

	@Test
	fun `runCatchingApi maps io exception to io failure`() {
		runCatchingApi<Int> {
			throw IOException("boom")
		} shouldBe Err(Failure.IoFailure)
	}

	@Test
	fun `receiver runCatchingApi maps unknown host exception to unknown host failure`() {
		Receiver(1).runCatchingApi {
			throw UnknownHostException("offline")
		} shouldBe Err(Failure.UnknownHostFailure)
	}

	@Test
	fun `runCatchingApi maps http exception to server error`() {
		val response = Response.error<String>(
			400,
			"""{"success":false,"status_code":400,"status_message":"bad request"}""".toResponseBody(
				"application/json".toMediaType()
			)
		)

		runCatchingApi<Int> {
			throw HttpException(response)
		} shouldBe Err(Failure.ServerError("bad request"))
	}

	@Test
	fun `runCatchingApi rethrows cancellation exception`() {
		val exception = CancellationException("cancelled")

		shouldThrow<CancellationException> {
			runCatchingApi<Int> {
				throw exception
			} shouldBe exception
		}
	}

	@Test
	fun `runCatchingApi rethrows unexpected exception`() {
		val exception = IllegalStateException("unexpected")

		shouldThrow<IllegalStateException> {
			runCatchingApi<Int> {
				throw exception
			}
		} shouldBe exception
	}

	@Test
	fun `runCatchingUnit wraps io exception in err`() {
		val exception = IOException("boom")

		runCatchingUnit<Unit> {
			throw exception
		} shouldBe Err(exception)
	}

	@Test
	fun `runCatchingUnit rethrows cancellation exception`() {
		val exception = CancellationException("cancelled")

		shouldThrow<CancellationException> {
			runCatchingUnit<Unit> {
				throw exception
			}
		} shouldBe exception
	}
}
