package com.zsoltbertalan.flickslate.ui

import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.kotlin.ext.test
import com.zsoltbertalan.flickslate.domain.api.MoviesRepository
import com.zsoltbertalan.flickslate.testhelper.MovieMother
import com.zsoltbertalan.flickslate.domain.model.MoviesResponse
import com.zsoltbertalan.flickslate.ui.movies.MoviesStoreFactory
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

class MoviesExecutorTest {

	private val moviesRepository = mockk<MoviesRepository>(relaxed = true)

	private lateinit var moviesExecutor: MoviesExecutor

	private lateinit var moviesStore: MoviesStore

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	@Before
	fun setUp() {
		coEvery { moviesRepository.getAllMovies() } answers {  MoviesResponse(MovieMother.createMovieList()) }

		moviesExecutor = MoviesExecutor(
			moviesRepository,
			testCoroutineDispatcher,
			testCoroutineDispatcher
		)

		moviesStore =
			MoviesStoreFactory(DefaultStoreFactory(), moviesExecutor).create(stateKeeper = StateKeeperDispatcher())
	}

	@Test
	fun `when started then getMovies is called and returns correct list`() {
		val states = moviesStore.states.test()

		coVerify(exactly = 1) { moviesRepository.getAllMovies() }
		states.first().movies shouldBe MovieMother.createMovieList()
	}

	@Test
	fun `when sort button is pressed then getMovies returned in reverse order`() {
		val states = moviesStore.states.test()

		moviesStore.accept(MoviesStore.Intent.SortMoviesClicked)

		states.last().movies shouldBe MovieMother.createMovieList().reversed()
	}

}


