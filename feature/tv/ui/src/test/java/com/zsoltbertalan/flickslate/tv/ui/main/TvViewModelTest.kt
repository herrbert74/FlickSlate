package com.zsoltbertalan.flickslate.tv.ui.main

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.shared.compose.component.paging.PaginationInternalState
import com.zsoltbertalan.flickslate.shared.model.Failure
import com.zsoltbertalan.flickslate.shared.model.PageData
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.tv.domain.api.TvRepository
import com.zsoltbertalan.flickslate.tv.domain.model.TvMother
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class TvViewModelTest {

	private val tvRepository = mockk<TvRepository>(relaxed = false)

	private lateinit var tvViewModel: TvViewModel

	private val dispatcher = StandardTestDispatcher()

	@Before
	fun setUp() {
		Dispatchers.setMain(dispatcher)

		coEvery { tvRepository.getTopRatedTv(any()) } answers {
			flowOf(Ok(PagingReply(TvMother.createTvList(), true, PageData())))
		}

		tvViewModel = TvViewModel(tvRepository)
	}

	@After
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun `when started then getMovies is called and returns correct list`() = runTest {
		tvViewModel.tvPaginationState.onRequestPage(tvViewModel.tvPaginationState, 0)

		advanceUntilIdle()
		coVerify(exactly = 1) { tvRepository.getTopRatedTv(any()) }

		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			tvViewModel.tvPaginationState.allItems shouldBeEqual TvMother.createTvList()
		}

	}

	@Test
	fun `when started and getMovies is called and call fails returns proper failure`() = runTest {
		coEvery { tvRepository.getTopRatedTv(any()) } answers {
			flowOf(Err(Failure.UnknownHostFailure))
		}
		tvViewModel.tvPaginationState.onRequestPage(tvViewModel.tvPaginationState, 0)

		advanceUntilIdle()
		coVerify(exactly = 1) { tvRepository.getTopRatedTv(any()) }

		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			shouldThrowExactly<NoSuchElementException> { tvViewModel.tvPaginationState.allItems }
			tvViewModel.tvPaginationState.internalState.value shouldBeEqualToComparingFields
				PaginationInternalState.Error(0, 0, Exception("Unknown host"), null)
		}

	}

}
