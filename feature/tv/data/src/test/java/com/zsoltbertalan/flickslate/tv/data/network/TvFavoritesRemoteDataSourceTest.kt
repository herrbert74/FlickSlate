package com.zsoltbertalan.flickslate.tv.data.network

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.shared.data.network.model.ErrorBody
import com.zsoltbertalan.flickslate.shared.kotlin.result.Failure
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

class TvFavoritesRemoteDataSourceTest {

	private val setTvFavoriteService: SetTvFavoriteService = mockk()
	private lateinit var tvFavoritesRemoteDataSource: TvFavoritesRemoteDataSource

	@Before
	fun setup() {
		coEvery { setTvFavoriteService.setTvFavorite(any(), any(), any()) } returns Unit
		tvFavoritesRemoteDataSource = TvFavoritesRemoteDataSource(setTvFavoriteService)
	}

	@Test
	fun `when setTvFavorite called and service returns result then returns correct result`() = runTest {
		tvFavoritesRemoteDataSource.setTvFavorite(
			accountId = 1,
			sessionId = "session",
			tvId = 10,
			favorite = true
		) shouldBeEqual Ok(Unit)
	}

	@Test
	fun `when setTvFavorite called and service throws http exception then returns correct result`() = runTest {
		coEvery { setTvFavoriteService.setTvFavorite(any(), any(), any()) } throws httpException()

		tvFavoritesRemoteDataSource.setTvFavorite(
			accountId = 1,
			sessionId = "session",
			tvId = 10,
			favorite = true
		) shouldBeEqual Err(Failure.ServerError("Invalid id"))
	}

	@Test
	fun `when setTvFavorite called and service throws UnknownHostException then returns correct result`() = runTest {
		coEvery { setTvFavoriteService.setTvFavorite(any(), any(), any()) } throws UnknownHostException()

		tvFavoritesRemoteDataSource.setTvFavorite(
			accountId = 1,
			sessionId = "session",
			tvId = 10,
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
