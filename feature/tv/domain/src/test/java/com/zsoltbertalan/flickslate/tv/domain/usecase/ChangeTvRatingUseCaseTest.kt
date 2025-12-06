package com.zsoltbertalan.flickslate.tv.domain.usecase

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.account.domain.usecase.GetSessionIdUseCase
import com.zsoltbertalan.flickslate.shared.kotlin.result.Failure
import com.zsoltbertalan.flickslate.tv.domain.api.TvRatingsRepository
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ChangeTvRatingUseCaseTest {
	private val getSessionIdUseCase: GetSessionIdUseCase = mockk()
	private val tvRatingsRepository: TvRatingsRepository = mockk()
	private val useCase = ChangeTvRatingUseCase(getSessionIdUseCase, tvRatingsRepository)

	@Test
	fun `execute returns success when dependencies succeed`() = runTest {
		coEvery { getSessionIdUseCase.execute() } returns Ok("session")
		coEvery { tvRatingsRepository.rateTvShow(tvShowId = 42, rating = 7.5f, sessionId = "session") } returns Ok(Unit)

		useCase.execute(tvShowId = 42, rating = 7.5f) shouldBeEqual Ok(Unit)
	}

	@Test
	fun `execute returns failure when session retrieval fails`() = runTest {
		val failure = Failure.UserNotLoggedIn
		coEvery { getSessionIdUseCase.execute() } returns Err(failure)

		useCase.execute(tvShowId = 42, rating = 7.5f) shouldBeEqual Err(failure)
	}

	@Test
	fun `execute returns failure when repository fails`() = runTest {
		val failure = Failure.ServerError("boom")
		coEvery { getSessionIdUseCase.execute() } returns Ok("session")
		coEvery { tvRatingsRepository.rateTvShow(tvShowId = 42, rating = 7.5f, sessionId = "session") } returns Err(failure)

		useCase.execute(tvShowId = 42, rating = 7.5f) shouldBeEqual Err(failure)
	}
}
