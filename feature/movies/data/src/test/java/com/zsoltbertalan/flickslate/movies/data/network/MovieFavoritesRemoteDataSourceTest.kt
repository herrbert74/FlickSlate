package com.zsoltbertalan.flickslate.movies.data.network

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.base.kotlin.result.Failure
import com.zsoltbertalan.flickslate.shared.data.network.model.ErrorBody
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.UnknownHostException

class MovieFavoritesRemoteDataSourceTest {

	private val setMovieFavoriteService: SetMovieFavoriteService = mockk()
	private lateinit var movieFavoritesRemoteDataSource: MovieFavoritesRemoteDataSource

	@Before
	fun setup() {
		coEvery { setMovieFavoriteService.setMovieFavorite(any(), any(), any()) } returns Unit
		movieFavoritesRemoteDataSource = MovieFavoritesRemoteDataSource(setMovieFavoriteService)
	}

	@Test
	fun `when setMovieFavorite called and service returns result then returns correct result`() = runTest {
		movieFavoritesRemoteDataSource.setMovieFavorite(
			accountId = 1,
			sessionId = "session",
			movieId = 10,
			favorite = true
		) shouldBeEqual Ok(Unit)
	}

	@Test
	fun `when setMovieFavorite called and service throws http exception then returns correct result`() = runTest {
		coEvery { setMovieFavoriteService.setMovieFavorite(any(), any(), any()) } throws httpException()

		movieFavoritesRemoteDataSource.setMovieFavorite(
			accountId = 1,
			sessionId = "session",
			movieId = 10,
			favorite = true
		) shouldBeEqual Err(Failure.ServerError("Invalid id"))
	}

	@Test
	fun `when setMovieFavorite called and service throws UnknownHostException then returns correct result`() = runTest {
		coEvery { setMovieFavoriteService.setMovieFavorite(any(), any(), any()) } throws UnknownHostException()

		movieFavoritesRemoteDataSource.setMovieFavorite(
			accountId = 1,
			sessionId = "session",
			movieId = 10,
			favorite = true
		) shouldBeEqual Err(Failure.UnknownHostFailure)
	}

	@Suppress("unused")
	private fun httpException(): HttpException {
		val errorBody = ErrorBody(
			success = false,
			status_code = 6,
			status_message = "Invalid id"
		)
		return HttpException(
			Response.error<Unit>(
				HttpURLConnection.HTTP_BAD_REQUEST,
				Json.encodeToString(errorBody).toResponseBody()
			)
		)
	}

}
