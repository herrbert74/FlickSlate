package com.zsoltbertalan.flickslate.movies.data.network.model

import com.zsoltbertalan.flickslate.shared.data.network.model.toPagingReply
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import io.kotest.matchers.shouldBe
import org.junit.Before
import org.junit.Test

class MovieReplyDtoTest {

	private lateinit var mappedReply: PagingReply<Movie>

	@Before
	fun setup() {
		val responseDto = MovieDtoMother.createMoviesReplyDto()
		mappedReply = responseDto.toPagingReply()
	}

	@Test
	fun `when there is a response then name is mapped`() {
		mappedReply.pagingList[0].title shouldBe "name1"
	}

	@Test
	fun `when there is a response then overview is mapped`() {
		mappedReply.pagingList[3].overview shouldBe "Overview 3"
	}

	@Test
	fun `when there is a response then posterPath is mapped`() {
		mappedReply.pagingList[0].posterPath shouldBe "/2w09J0KUnVtJvqPYu8N63XjAyCR.jpg"
	}

}
