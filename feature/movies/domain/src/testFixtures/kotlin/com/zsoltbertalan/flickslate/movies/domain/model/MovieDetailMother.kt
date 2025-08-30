package com.zsoltbertalan.flickslate.movies.domain.model

import com.zsoltbertalan.flickslate.shared.domain.model.Genre
import kotlinx.collections.immutable.toImmutableList

object MovieDetailMother {

	fun createMovieDetail(
		id: Int = 0,
		title: String = "Brazil",
		overview: String = "Best film ever",
		voteAverage: Float = 8.7800f,
		posterPath: String = "app1",
		backdropPath: String = "/ziRWOYnl6e2JUaHYmFLR1kfcECM.jpg",
	): MovieDetail = MovieDetail(
		id = id,
		title = title,
		overview = overview,
		voteAverage = voteAverage,
		posterPath = posterPath,
		backdropPath = backdropPath,
		genres = listOf(
			Genre(1, "Adventure"),
			Genre(2, "Comedy")
		).toImmutableList()
	)

}
