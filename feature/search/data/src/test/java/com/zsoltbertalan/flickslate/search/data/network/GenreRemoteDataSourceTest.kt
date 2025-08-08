package com.zsoltbertalan.flickslate.search.data.network

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.shared.model.Failure
import com.zsoltbertalan.flickslate.shared.model.GenreMother
import com.zsoltbertalan.flickslate.shared.model.GenresReply
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class GenreRemoteDataSourceTest {

	private val searchService: SearchService = mockk()
	private lateinit var genreRemoteDataSource: GenreRemoteDataSource

	@Before
	fun setup() {
		coEvery { searchService.getGenres(any(), any()) } returns Response.success(GenreDtoMother.createGenreReplyDto())
		genreRemoteDataSource = GenreRemoteDataSource(searchService)
	}

	@Test
	fun `when getGenres called and service returns result then returns correct result`() = runTest {
		genreRemoteDataSource.getGenres("") shouldBeEqual Ok(GenresReply(GenreMother.createGenreList(), ""))
	}

	@Test
	fun `when getGenres called and service returns failure then returns correct result`() = runTest {
		coEvery { searchService.getGenres(any(), any()) } returns failNetworkRequestResponse()()
		genreRemoteDataSource.getGenres("") shouldBeEqual
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

}
