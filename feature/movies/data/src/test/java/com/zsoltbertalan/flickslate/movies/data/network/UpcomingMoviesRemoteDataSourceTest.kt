package com.zsoltbertalan.flickslate.movies.data.network

import com.github.michaelbull.result.Err
import com.zsoltbertalan.flickslate.movies.data.network.model.MovieDtoMother
import com.zsoltbertalan.flickslate.movies.data.network.model.TOTAL_PAGES
import com.zsoltbertalan.flickslate.movies.data.network.model.TOTAL_RESULTS
import com.zsoltbertalan.flickslate.movies.data.network.model.UpcomingMoviesReplyDto
import com.zsoltbertalan.flickslate.movies.domain.model.MovieMother
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

class UpcomingMoviesRemoteDataSourceTest {

	private val moviesService: MoviesService = mockk()
	private lateinit var upcomingMoviesRemoteDataSource: UpcomingMoviesRemoteDataSource

	@Before
	fun setup() {
		coEvery { moviesService.getUpcomingMovies(any(), any(), any()) } returns Response.success(
			MovieDtoMother.createUpcomingMovieList()
		)
		upcomingMoviesRemoteDataSource = UpcomingMoviesRemoteDataSource(moviesService)
	}

	@Test
	fun `when getUpcomingMovies called and service returns result then returns correct result`() = runTest {
		val result = upcomingMoviesRemoteDataSource.getUpcomingMovies("", 1)
		result.value.pagingList shouldBeEqual MovieMother.createUpcomingMovieList()
		result.value.isLastPage shouldBe false
		result.value.pageData shouldBe PageData(totalPages = TOTAL_PAGES, totalResults = TOTAL_RESULTS)
	}

	@Test
	fun `when getUpcomingMovies called and service returns failure then returns correct result`() = runTest {
		coEvery { moviesService.getUpcomingMovies(any(), any(), any()) } returns failNetworkRequestResponse()()
		upcomingMoviesRemoteDataSource.getUpcomingMovies("", 1) shouldBeEqual
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
	private fun failNetworkRequestResponse(): () -> Response<UpcomingMoviesReplyDto> = {
		val errorBody = ErrorBody(
			success = false,
			status_code = 6,
			status_message = "Invalid id: The pre-requisite id is invalid or not found."
		)
		Response.error(404, Json.encodeToString(errorBody).toResponseBody())
	}

}
