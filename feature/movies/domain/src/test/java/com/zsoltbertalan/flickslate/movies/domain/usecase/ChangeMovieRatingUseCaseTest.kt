package com.zsoltbertalan.flickslate.movies.domain.usecase

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.account.domain.usecase.GetSessionIdUseCase
import com.zsoltbertalan.flickslate.movies.domain.api.MovieRatingsRepository
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ChangeMovieRatingUseCaseTest {
	private lateinit var getSessionIdUseCase: GetSessionIdUseCase
	private lateinit var movieRatingsRepository: MovieRatingsRepository
	private lateinit var useCase: ChangeMovieRatingUseCase

	@Before
	fun setUp() {
		getSessionIdUseCase = mockk()
		movieRatingsRepository = mockk()
		useCase = ChangeMovieRatingUseCase(getSessionIdUseCase, movieRatingsRepository)
	}

	@Test
	fun `execute should call repository with correct params and return outcome`() = runBlocking {
		val movieId = 1
		val rating = 4.5f
		val sessionId = "session123"
		val expectedOutcome: Outcome<Unit> = Ok(Unit)

		coEvery { getSessionIdUseCase.execute() } returns Ok(sessionId)
		coEvery { movieRatingsRepository.rateMovie(movieId, rating, sessionId) } returns expectedOutcome

		val result = useCase.execute(movieId, rating)

		result shouldBe expectedOutcome
		coVerify { getSessionIdUseCase.execute() }
		coVerify { movieRatingsRepository.rateMovie(movieId, rating, sessionId) }
	}
}
