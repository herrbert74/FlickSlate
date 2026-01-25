package com.zsoltbertalan.flickslate.movies.domain.usecase

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.account.domain.usecase.GetSessionIdUseCase
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.movies.domain.api.MovieRatingsRepository
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DeleteMovieRatingUseCaseTest {
	private lateinit var getSessionIdUseCase: GetSessionIdUseCase
	private lateinit var movieRatingsRepository: MovieRatingsRepository
	private lateinit var useCase: DeleteMovieRatingUseCase

	@Before
	fun setUp() {
		getSessionIdUseCase = mockk()
		movieRatingsRepository = mockk()
		useCase = DeleteMovieRatingUseCase(getSessionIdUseCase, movieRatingsRepository)
	}

	@Test
	fun `execute should call repository with correct params and return outcome`() = runBlocking {
		val movieId = 1
		val sessionId = "session123"
		val expectedOutcome: Outcome<Unit> = Ok(Unit)

		coEvery { getSessionIdUseCase.execute() } returns Ok(sessionId)
		coEvery { movieRatingsRepository.deleteMovieRating(movieId, sessionId) } returns expectedOutcome

		val result = useCase.execute(movieId)

		result shouldBe expectedOutcome
		coVerify { getSessionIdUseCase.execute() }
		coVerify { movieRatingsRepository.deleteMovieRating(movieId, sessionId) }
	}
}
