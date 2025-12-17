package com.zsoltbertalan.flickslate.account.data.repository

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.account.data.api.FavoritesDataSource
import com.zsoltbertalan.flickslate.account.domain.model.FavoriteMovie
import com.zsoltbertalan.flickslate.account.domain.model.FavoriteTvShow
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FavoritesAccessorTest {

	private val favoritesRemoteDataSource: FavoritesDataSource.Remote = mockk()

	private lateinit var favoritesAccessor: FavoritesAccessor

	@Before
	fun setup() {
		favoritesAccessor = FavoritesAccessor(
			favoritesRemoteDataSource,
		)
	}

	@Test
	fun `when getFavoriteMovies called then delegates to remote datasource`() = runTest {
		val accountId = 123
		val sessionId = "session-id"
		val page = 2
		val reply = PagingReply(
			pagingList = listOf(
				FavoriteMovie(Movie(id = 1, title = "Movie 1")),
				FavoriteMovie(Movie(id = 2, title = "Movie 2")),
			),
			isLastPage = false,
			pageData = PageData(page = page),
		)

		coEvery {
			favoritesRemoteDataSource.getFavoriteMovies(accountId, sessionId, page)
		} returns Ok(reply)

		val result = favoritesAccessor.getFavoriteMovies(accountId, sessionId, page)
		result shouldBeEqual Ok(reply)
		coVerify(exactly = 1) { favoritesRemoteDataSource.getFavoriteMovies(accountId, sessionId, page) }
	}

	@Test
	fun `when getFavoriteTvShows called then delegates to remote datasource`() = runTest {
		val accountId = 456
		val sessionId = "session-id"
		val page = 1
		val reply = PagingReply(
			pagingList = listOf(
				FavoriteTvShow(TvShow(id = 10, name = "Show 10")),
				FavoriteTvShow(TvShow(id = 11, name = "Show 11")),
			),
			isLastPage = true,
			pageData = PageData(page = page),
		)

		coEvery {
			favoritesRemoteDataSource.getFavoriteTvShows(accountId, sessionId, page)
		} returns Ok(reply)

		val result = favoritesAccessor.getFavoriteTvShows(accountId, sessionId, page)
		result shouldBeEqual Ok(reply)
		coVerify(exactly = 1) { favoritesRemoteDataSource.getFavoriteTvShows(accountId, sessionId, page) }
	}
}
