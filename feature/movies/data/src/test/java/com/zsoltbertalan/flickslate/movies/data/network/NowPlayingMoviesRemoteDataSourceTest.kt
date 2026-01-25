package com.zsoltbertalan.flickslate.movies.data.network

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.zsoltbertalan.flickslate.base.kotlin.result.Failure
import com.zsoltbertalan.flickslate.movies.data.network.model.MovieDtoMother
import com.zsoltbertalan.flickslate.movies.data.network.model.NowPlayingMoviesReplyDto
import com.zsoltbertalan.flickslate.movies.data.network.model.TOTAL_PAGES
import com.zsoltbertalan.flickslate.movies.data.network.model.TOTAL_RESULTS
import com.zsoltbertalan.flickslate.shared.data.network.model.ErrorBody
import com.zsoltbertalan.flickslate.shared.domain.model.MovieMother
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
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

class NowPlayingMoviesRemoteDataSourceTest {

	private val moviesService: MoviesService = mockk()
	private lateinit var nowPlayingMoviesRemoteDataSource: NowPlayingMoviesRemoteDataSource

	@Before
	fun setup() {
		coEvery { moviesService.getNowPlayingMovies(any(), any(), any()) } returns Response.success(
			MovieDtoMother.createNowPlayingMovieList()
		)
		nowPlayingMoviesRemoteDataSource = NowPlayingMoviesRemoteDataSource(moviesService)
	}

	@Test
	fun `when getNowPlayingMovies called and service returns result then returns correct result`() = runTest {
		val result = nowPlayingMoviesRemoteDataSource.getNowPlayingMovies("", 1)
		result
			.onSuccess {
				it.pagingList shouldBeEqual MovieMother.createNowPlayingMovieList()
				it.isLastPage shouldBe false
				it.pageData shouldBe PageData(totalPages = TOTAL_PAGES, totalResults = TOTAL_RESULTS)
			}
			.onFailure {
				throw AssertionError("Expected Ok but was Err($it)")
			}
	}

	@Test
	fun `when getNowPlayingMovies called and service returns failure then returns correct result`() = runTest {
		coEvery { moviesService.getNowPlayingMovies(any(), any(), any()) } returns failNetworkRequestResponse()()
		nowPlayingMoviesRemoteDataSource.getNowPlayingMovies("", 1) shouldBeEqual
			Err(
				Failure.ServerError(
					"""
						{"success":false,
						"status_code":6,"status_message":"Invalid id: The pre-requisite id is invalid
						 or not found."}
						 """
						.trimIndent()
						.filterNot { it == '\n' }
				)
			)
	}

	@Suppress("unused")
	private fun failNetworkRequestResponse(): () -> Response<NowPlayingMoviesReplyDto> = {
		val errorBody = ErrorBody(
			success = false,
			status_code = 6,
			status_message = "Invalid id: The pre-requisite id is invalid or not found."
		)
		Response.error(404, Json.encodeToString(errorBody).toResponseBody())
	}

}
