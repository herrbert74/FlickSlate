package com.zsoltbertalan.flickslate.movies.data.network.model

import com.zsoltbertalan.flickslate.shared.data.network.model.toMoviesReply
import com.zsoltbertalan.flickslate.shared.model.Movie
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import io.kotest.matchers.shouldBe
import org.junit.Before
import org.junit.Test

class MovieResponseDtoTest {

	private lateinit var mappedResponse: PagingReply<Movie>

	@Before
	fun setup() {
		val responseDto = MovieDtoMother.createMoviesResponseDto()
		mappedResponse = responseDto.toMoviesReply()
	}

	@Test
	fun `when there is a response then name is mapped`() {
		mappedResponse.pagingList[0].title shouldBe "name1"
	}

	@Test
	fun `when there is a response then overview is mapped`() {
		mappedResponse.pagingList[3].overview shouldBe "Overview 3"
	}

	@Test
	fun `when there is a response then posterPath is mapped`() {
		mappedResponse.pagingList[0].posterPath shouldBe "/2w09J0KUnVtJvqPYu8N63XjAyCR.jpg"
	}

}
