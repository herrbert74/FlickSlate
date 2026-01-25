package com.zsoltbertalan.flickslate.tv.data.network

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.base.kotlin.result.Failure
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class TvRatingsRemoteDataSourceTest {

	private val tvService: TvService = mockk()
	private lateinit var remoteDataSource: TvRatingsRemoteDataSource

	@Before
	fun setup() {
		remoteDataSource = TvRatingsRemoteDataSource(tvService)
	}

	@Test
	fun `rateTvShow returns success`() = runTest {
		coEvery { tvService.rateTvShow(any(), any(), any()) } returns Unit

		val result = remoteDataSource.rateTvShow(tvShowId = 1, rating = 8.5f, sessionId = "session")

		result shouldBe Ok(Unit)
	}

	@Test
	fun `rateTvShow returns failure`() = runTest {
		coEvery { tvService.rateTvShow(any(), any(), any()) } throws httpException()

		val result = remoteDataSource.rateTvShow(tvShowId = 1, rating = 8.5f, sessionId = "session")

		result shouldBe Err(Failure.ServerError("Server boom"))
	}

	@Test
	fun `rateTvShowEpisode returns success`() = runTest {
		coEvery { tvService.rateTvShowEpisode(any(), any(), any(), any(), any()) } returns Unit

		remoteDataSource.rateTvShowEpisode(1, 2, 3, 7.5f, "session") shouldBe Ok(Unit)
	}

	@Test
	fun `rateTvShowEpisode returns failure`() = runTest {
		coEvery { tvService.rateTvShowEpisode(any(), any(), any(), any(), any()) } throws httpException()

		remoteDataSource.rateTvShowEpisode(1, 2, 3, 7.5f, "session") shouldBe Err(Failure.ServerError("Server boom"))
	}

	@Test
	fun `deleteTvShowRating returns success`() = runTest {
		coEvery { tvService.deleteTvShowRating(any(), any()) } returns Unit

		remoteDataSource.deleteTvShowRating(1, "session") shouldBe Ok(Unit)
	}

	@Test
	fun `deleteTvShowRating returns failure`() = runTest {
		coEvery { tvService.deleteTvShowRating(any(), any()) } throws httpException()

		remoteDataSource.deleteTvShowRating(1, "session") shouldBe Err(Failure.ServerError("Server boom"))
	}

	@Test
	fun `deleteTvShowEpisodeRating returns success`() = runTest {
		coEvery { tvService.deleteTvShowEpisodeRating(any(), any(), any(), any()) } returns Unit

		remoteDataSource.deleteTvShowEpisodeRating(1, 2, 3, "session") shouldBe Ok(Unit)
	}

	@Test
	fun `deleteTvShowEpisodeRating returns failure`() = runTest {
		coEvery { tvService.deleteTvShowEpisodeRating(any(), any(), any(), any()) } throws httpException()

		remoteDataSource.deleteTvShowEpisodeRating(1, 2, 3, "session") shouldBe Err(Failure.ServerError("Server boom"))
	}

	private fun httpException(): HttpException {
		val response = Response.error<Unit>(
			500,
			"""{"success":false,"status_code":6,"status_message":"Server boom"}"""
				.toResponseBody("application/json".toMediaType())
		)
		return HttpException(response)
	}
}
