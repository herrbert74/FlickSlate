package com.zsoltbertalan.flickslate.tv.data.repository

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.base.kotlin.result.Failure
import com.zsoltbertalan.flickslate.tv.data.api.TvDataSource
import com.zsoltbertalan.flickslate.tv.domain.model.TvMother
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TvAccessorTest {

	private val tvLocalDataSource: TvDataSource.Local = mockk()
	private val tvRemoteDataSource: TvDataSource.Remote = mockk()

	private lateinit var tvAccessor: TvAccessor

	@Before
	fun setup() {
		coEvery { tvLocalDataSource.getEtag(any()) } returns null
		coEvery { tvLocalDataSource.insertTv(any(), any()) } returns Unit
		coEvery { tvLocalDataSource.insertTvPageData(any()) } returns Unit
		coEvery { tvLocalDataSource.getTv(any()) } returns flowOf(null)
		coEvery { tvRemoteDataSource.getTopRatedTv(any(), any()) } returns Ok(
			PagingReply(TvMother.createTvList(), true, PageData())
		)
		tvAccessor = TvAccessor(
			tvLocalDataSource,
			tvRemoteDataSource,
		)
	}

	@Test
	fun `when getTopRatedTv called successfully then returns correct result`() = runTest {
		val topRatedTvFlow = tvAccessor.getTopRatedTv(1)
		val pagingData = TvMother.createTvList()
		topRatedTvFlow.first()
			.onSuccess {
				it.pagingList shouldBeEqual pagingData
			}
			.onFailure {
				throw AssertionError("Expected Ok but was Err($it)")
			}
	}

	@Test
	fun `when getTopRatedTv fails but local has data then returns local data`() = runTest {
		coEvery { tvRemoteDataSource.getTopRatedTv(any(), any()) } returns
			Err(Failure.ServerError("Network error"))

		val localTvShows = TvMother.createTvList()
		val localPagingReply = PagingReply(localTvShows, true, PageData(page = 1, totalPages = 1, totalResults = 3))
		coEvery { tvLocalDataSource.getTv(any()) } returns flowOf(localPagingReply)

		val topRatedTvFlow = tvAccessor.getTopRatedTv(1)

		val result = topRatedTvFlow.first()
		result
			.onSuccess {
				it.pagingList shouldBeEqual localTvShows
				it.isLastPage shouldBeEqual true
				it.pageData.page shouldBeEqual 1
			}
			.onFailure {
				throw AssertionError("Expected Ok but was Err($it)")
			}
	}

	@Test
	fun `getTvDetails returns remote result`() = runTest {
		val detail = TvMother.createTvDetail()
		coEvery { tvRemoteDataSource.getTvDetails(any(), any()) } returns Ok(detail)

		tvAccessor.getTvDetails(seriesId = 1, sessionId = "session") shouldBeEqual Ok(detail)
	}

	@Test
	fun `getTvDetails propagates error`() = runTest {
		val failure = Failure.ServerError("error")
		coEvery { tvRemoteDataSource.getTvDetails(any(), any()) } returns Err(failure)

		tvAccessor.getTvDetails(seriesId = 1, sessionId = "session") shouldBeEqual Err(failure)
	}

	@Test
	fun `getTvImages returns remote result`() = runTest {
		val images = TvMother.createTvImages()
		coEvery { tvRemoteDataSource.getTvImages(any()) } returns Ok(images)

		val result = tvAccessor.getTvImages(seriesId = 1)

		result shouldBeEqual Ok(images)
	}

	@Test
	fun `getTvImages propagates error`() = runTest {
		val failure = Failure.ServerError("error")
		coEvery { tvRemoteDataSource.getTvImages(any()) } returns Err(failure)

		tvAccessor.getTvImages(seriesId = 1) shouldBeEqual Err(failure)
	}

	@Test
	fun `getTvSeasonDetail returns remote result`() = runTest {
		val season = TvMother.createSeasonDetail(seriesId = 1, seasonNumber = 1)
		coEvery { tvRemoteDataSource.getTvSeasonDetails(any(), any()) } returns Ok(season)

		tvAccessor.getTvSeasonDetail(seriesId = 1, seasonNumber = 1) shouldBeEqual Ok(season)
	}

	@Test
	fun `getTvSeasonDetail propagates error`() = runTest {
		val failure = Failure.ServerError("error")
		coEvery { tvRemoteDataSource.getTvSeasonDetails(any(), any()) } returns Err(failure)

		tvAccessor.getTvSeasonDetail(seriesId = 1, seasonNumber = 1) shouldBeEqual Err(failure)
	}

	@Test
	fun `getTvEpisodeDetail returns remote result`() = runTest {
		val episode = TvMother.createSeasonDetail(1, 1).episodes.first()
		coEvery { tvRemoteDataSource.getTvEpisodeDetail(any(), any(), any(), any()) } returns Ok(episode)

		tvAccessor.getTvEpisodeDetail(1, 1, 1, "session") shouldBeEqual Ok(episode)
	}

	@Test
	fun `getTvEpisodeDetail propagates error`() = runTest {
		val failure = Failure.ServerError("error")
		coEvery { tvRemoteDataSource.getTvEpisodeDetail(any(), any(), any(), any()) } returns Err(failure)

		tvAccessor.getTvEpisodeDetail(1, 1, 1, "session") shouldBeEqual Err(failure)
	}

}
