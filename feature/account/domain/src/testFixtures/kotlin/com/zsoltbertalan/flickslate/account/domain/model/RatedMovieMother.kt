package com.zsoltbertalan.flickslate.account.domain.model

import com.zsoltbertalan.flickslate.shared.domain.model.MovieMother

object RatedMovieMother {

	fun createRatedMovieList(): List<RatedMovie> {
		return listOf(
			RatedMovie(movie = MovieMother.createDefaultMovie(), rating = 10f),
		)
	}

}
