package com.zsoltbertalan.flickslate.account.domain.model

import com.zsoltbertalan.flickslate.shared.domain.model.MovieMother
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply

object RatedMovieMother {

	fun createRatedMovieList(): PagingReply<RatedMovie> {
		return PagingReply(
			listOf(RatedMovie(movie = MovieMother.createDefaultMovie(), rating = 10f)),
			isLastPage = true,
			PageData()
		)
	}

}
