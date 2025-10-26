package com.zsoltbertalan.flickslate.account.ui.ratings

import com.zsoltbertalan.flickslate.account.domain.model.RatedMovie
import com.zsoltbertalan.flickslate.shared.domain.model.MovieMother

object RatedMovieMother {

	fun createRatedMovieList(): List<RatedMovie> {
		return listOf(
			RatedMovie(movie = MovieMother.createPopularMovieList()[0], rating = 7.5f),
			RatedMovie(movie = MovieMother.createPopularMovieList()[1], rating = 8.0f)
		)
	}

}
