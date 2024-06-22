package com.zsoltbertalan.flickslate.mappers

import com.zsoltbertalan.flickslate.testhelper.MovieDtoMother
import com.zsoltbertalan.flickslate.data.network.dto.toMoviesResponse
import com.zsoltbertalan.flickslate.domain.model.MoviesResponse
import io.kotest.matchers.shouldBe
import org.junit.Before
import org.junit.Test

class MappersTest {

	private var mappedResponse : MoviesResponse = MoviesResponse()
	@Before
	fun setup() {
		val responseDto = MovieDtoMother.createMoviesResponseDto()
		mappedResponse = responseDto.toMoviesResponse()
	}

	@Test
	fun `when there is a response then name is mapped`() {
		mappedResponse.movies[0].title shouldBe "name1"
	}

	@Test
	fun `when there is a response then overview is mapped`() {
		mappedResponse.movies[3].overview shouldBe "Overview 3"
	}

	@Test
	fun `when there is a response then posterPath is mapped`() {
		mappedResponse.movies[0].posterPath shouldBe "app1"
	}

}
