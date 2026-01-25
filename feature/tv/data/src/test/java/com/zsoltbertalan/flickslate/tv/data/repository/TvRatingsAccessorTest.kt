package com.zsoltbertalan.flickslate.tv.data.repository

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.base.kotlin.result.Failure
import com.zsoltbertalan.flickslate.tv.data.api.TvRatingsDataSource
import com.zsoltbertalan.flickslate.tv.domain.api.TvRatingsRepository
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TvRatingsAccessorTest {

	private val remoteDataSource: TvRatingsDataSource.Remote = mockk()
	private lateinit var repository: TvRatingsRepository

	@Before
	fun setup() {
		repository = TvRatingsAccessor(remoteDataSource)
	}

	@Test
	fun `rateTvShow forwards success`() = runTest {
		coEvery { remoteDataSource.rateTvShow(any(), any(), any()) } returns Ok(Unit)

		repository.rateTvShow(1, 8.5f, "session") shouldBe Ok(Unit)
	}

	@Test
	fun `rateTvShow forwards failure`() = runTest {
		coEvery { remoteDataSource.rateTvShow(any(), any(), any()) } returns Err(Failure.ServerError("boom"))

		repository.rateTvShow(1, 8.5f, "session") shouldBe Err(Failure.ServerError("boom"))
	}

	@Test
	fun `rateTvShowEpisode forwards success`() = runTest {
		coEvery { remoteDataSource.rateTvShowEpisode(any(), any(), any(), any(), any()) } returns Ok(Unit)

		repository.rateTvShowEpisode(1, 2, 3, 7.5f, "session") shouldBe Ok(Unit)
	}

	@Test
	fun `rateTvShowEpisode forwards failure`() = runTest {
		coEvery {
			remoteDataSource.rateTvShowEpisode(
				any(),
				any(),
				any(),
				any(),
				any()
			)
		} returns Err(Failure.ServerError("boom"))

		repository.rateTvShowEpisode(1, 2, 3, 7.5f, "session") shouldBe Err(Failure.ServerError("boom"))
	}

	@Test
	fun `deleteTvShowRating forwards success`() = runTest {
		coEvery { remoteDataSource.deleteTvShowRating(any(), any()) } returns Ok(Unit)

		repository.deleteTvShowRating(1, "session") shouldBe Ok(Unit)
	}

	@Test
	fun `deleteTvShowRating forwards failure`() = runTest {
		coEvery { remoteDataSource.deleteTvShowRating(any(), any()) } returns Err(Failure.ServerError("boom"))

		repository.deleteTvShowRating(1, "session") shouldBe Err(Failure.ServerError("boom"))
	}

	@Test
	fun `deleteTvShowEpisodeRating forwards success`() = runTest {
		coEvery { remoteDataSource.deleteTvShowEpisodeRating(any(), any(), any(), any()) } returns Ok(Unit)

		repository.deleteTvShowEpisodeRating(1, 2, 3, "session") shouldBe Ok(Unit)
	}

	@Test
	fun `deleteTvShowEpisodeRating forwards failure`() = runTest {
		coEvery {
			remoteDataSource.deleteTvShowEpisodeRating(
				any(),
				any(),
				any(),
				any()
			)
		} returns Err(Failure.ServerError("boom"))

		repository.deleteTvShowEpisodeRating(1, 2, 3, "session") shouldBe Err(Failure.ServerError("boom"))
	}
}
