package com.zsoltbertalan.flickslate.account.data.network

import com.github.michaelbull.result.Err
import com.zsoltbertalan.flickslate.account.data.network.model.FavoriteMovieReplyDto
import com.zsoltbertalan.flickslate.account.data.network.model.FavoriteTvShowReplyDto
import com.zsoltbertalan.flickslate.account.data.network.model.FavoriteMovieDtoMother
import com.zsoltbertalan.flickslate.account.data.network.model.FavoriteMovieReplyDtoMother
import com.zsoltbertalan.flickslate.account.data.network.model.FavoriteTvShowDtoMother
import com.zsoltbertalan.flickslate.account.data.network.model.FavoriteTvShowReplyDtoMother
import com.zsoltbertalan.flickslate.account.data.network.model.toFavoriteMovieList
import com.zsoltbertalan.flickslate.account.data.network.model.toFavoriteTvShowList
import com.zsoltbertalan.flickslate.shared.data.network.model.ErrorBody
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.kotlin.result.Failure
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class FavoritesRemoteDataSourceTest {

	private val favoritesService: FavoritesService = mockk()
	private lateinit var favoritesRemoteDataSource: FavoritesRemoteDataSource

	@Before
	fun setup() {
		coEvery { favoritesService.getFavoriteMovies(any(), any(), any()) } returns Response.success(
			FavoriteMovieReplyDtoMother.createFavoriteMovieReplyDto()
		)
		coEvery { favoritesService.getFavoriteTvShows(any(), any(), any()) } returns Response.success(
			FavoriteTvShowReplyDtoMother.createFavoriteTvShowReplyDto()
		)
		favoritesRemoteDataSource = FavoritesRemoteDataSource(favoritesService)
	}

	@Test
	fun `when getFavoriteMovies called and service returns result then returns correct result`() = runTest {
		val result = favoritesRemoteDataSource.getFavoriteMovies(accountId = 1, sessionId = "session", page = 1)

		val movies = listOf(FavoriteMovieDtoMother.createFavoriteMovieDto())
		result.value.pagingList shouldBeEqual movies.toFavoriteMovieList()
		result.value.isLastPage shouldBe false
		result.value.pageData shouldBe PageData(page = 1, totalPages = 2, totalResults = 1)
	}

	@Test
	fun `when getFavoriteMovies called and service returns failure then returns correct result`() = runTest {
		coEvery { favoritesService.getFavoriteMovies(any(), any(), any()) } returns failNetworkRequestResponseMovie()()

		val errorBody = ErrorBody(
			success = false,
			status_code = 6,
			status_message = "Invalid id: The pre-requisite id is invalid or not found."
		)
		favoritesRemoteDataSource.getFavoriteMovies(accountId = 1, sessionId = "session", page = 1) shouldBeEqual
			Err(Failure.ServerError(Json.encodeToString(errorBody)))
	}

	@Test
	fun `when getFavoriteTvShows called and service returns result then returns correct result`() = runTest {
		val result = favoritesRemoteDataSource.getFavoriteTvShows(accountId = 1, sessionId = "session", page = 1)

		val tvShows = listOf(FavoriteTvShowDtoMother.createFavoriteTvShowDto())
		result.value.pagingList shouldBeEqual tvShows.toFavoriteTvShowList()
		result.value.isLastPage shouldBe false
		result.value.pageData shouldBe PageData(page = 1, totalPages = 2, totalResults = 1)
	}

	@Test
	fun `when getFavoriteTvShows called and service returns failure then returns correct result`() = runTest {
		coEvery { favoritesService.getFavoriteTvShows(any(), any(), any()) } returns failNetworkRequestResponseTv()()

		val errorBody = ErrorBody(
			success = false,
			status_code = 6,
			status_message = "Invalid id: The pre-requisite id is invalid or not found."
		)
		favoritesRemoteDataSource.getFavoriteTvShows(accountId = 1, sessionId = "session", page = 1) shouldBeEqual
			Err(Failure.ServerError(Json.encodeToString(errorBody)))
	}

	@Suppress("unused")
	private fun failNetworkRequestResponseMovie(): () -> Response<FavoriteMovieReplyDto> = {
		val errorBody = ErrorBody(
			success = false,
			status_code = 6,
			status_message = "Invalid id: The pre-requisite id is invalid or not found."
		)
		Response.error(404, Json.encodeToString(errorBody).toResponseBody())
	}

	@Suppress("unused")
	private fun failNetworkRequestResponseTv(): () -> Response<FavoriteTvShowReplyDto> = {
		val errorBody = ErrorBody(
			success = false,
			status_code = 6,
			status_message = "Invalid id: The pre-requisite id is invalid or not found."
		)
		Response.error(404, Json.encodeToString(errorBody).toResponseBody())
	}

}
