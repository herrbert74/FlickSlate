package com.zsoltbertalan.flickslate.tv.data.repository

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.shared.model.Failure
import com.zsoltbertalan.flickslate.shared.model.PageData
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.tv.data.api.TvDataSource
import com.zsoltbertalan.flickslate.tv.data.network.TvService
import com.zsoltbertalan.flickslate.tv.data.network.model.TvDtoMother
import com.zsoltbertalan.flickslate.tv.domain.model.TvMother
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class TvAccessorTest {

	private val tvService: TvService = mockk()

	private val tvLocalDataSource: TvDataSource.Local = mockk()
	private val tvRemoteDataSource: TvDataSource.Remote = mockk()

	private lateinit var tvAccessor: TvAccessor

	@Before
	fun setup() {
		coEvery {
			tvService.getTopRatedTv(any(), any(), any())
		} returns Response.success(TvDtoMother.createTopRatedTvReplyDto())
		coEvery { tvLocalDataSource.getEtag(any()) } returns null
		coEvery { tvLocalDataSource.insertTv(any(), any()) } returns Unit
		coEvery { tvLocalDataSource.insertTvPageData(any()) } returns Unit
		coEvery { tvLocalDataSource.getTv(any()) } returns flowOf(null)
		coEvery { tvRemoteDataSource.getTopRatedTv(any(), any()) } returns Ok(
			PagingReply(TvMother.createTvList(), true, PageData())
		)
		tvAccessor = TvAccessor(
			tvService,
			tvLocalDataSource,
			tvRemoteDataSource,
		)
	}

	@Test
	fun `when getTopRatedTv called successfully then returns correct result`() = runTest {
		val topRatedTvFlow = tvAccessor.getTopRatedTv(1)
		val pagingData = TvMother.createTvList()
		topRatedTvFlow.first().value.pagingList shouldBeEqual pagingData
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
		result.value.pagingList shouldBeEqual localTvShows
		result.value.isLastPage shouldBeEqual true
		result.value.pageData.page shouldBeEqual 1
	}

}
