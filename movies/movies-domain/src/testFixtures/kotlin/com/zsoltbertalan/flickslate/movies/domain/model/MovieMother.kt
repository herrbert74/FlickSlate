package com.zsoltbertalan.flickslate.movies.domain.model

import com.zsoltbertalan.flickslate.shared.model.Genre
import com.zsoltbertalan.flickslate.shared.model.Movie
import kotlinx.collections.immutable.toImmutableList

/**
 * This is an example of an ObjectMother that can be used in both Unit and Android UI tests.
 * As such it would go into its own module in a normal project.
 */
object MovieMother {

	fun createMovieList() = listOf(
		createDefaultMovie(id = 0, title = "name1", overview = "Overview 0"),
		createDefaultMovie(id = 1, title = "name2"),
		createDefaultMovie(id = 2, title = "name3", overview = "Overview 2"),
		createDefaultMovie(id = 3, title = "name4", overview = "Overview 3"),
		createDefaultMovie(id = 4, title = "name5"),
		createDefaultMovie(id = 5, title = "name6"),
		createDefaultMovie(id = 6, title = "name6"),
		createDefaultMovie(id = 7, title = "name6"),
		createDefaultMovie(id = 8, title = "name6"),
		createDefaultMovie(id = 9, title = "name6"),
		createDefaultMovie(id = 10, title = "name6"),
		createDefaultMovie(id = 11, title = "name6"),
		createDefaultMovie(id = 12, title = "name6"),
		createDefaultMovie(id = 13, title = "name6"),
	)

	fun createPopularMovieList() = listOf(
		createDefaultMovie(id = 0, title = "name101", overview = "Overview 0"),
		createDefaultMovie(id = 1, title = "name102"),
		createDefaultMovie(id = 2, title = "name103", overview = "Overview 2"),
		createDefaultMovie(id = 3, title = "name104", overview = "Overview 3"),
		createDefaultMovie(id = 4, title = "name105"),
		createDefaultMovie(id = 5, title = "name106"),
		createDefaultMovie(id = 6, title = "name6"),
		createDefaultMovie(id = 7, title = "name6"),
		createDefaultMovie(id = 8, title = "name6"),
		createDefaultMovie(id = 9, title = "name6"),
		createDefaultMovie(id = 10, title = "name6"),
		createDefaultMovie(id = 11, title = "name6"),
		createDefaultMovie(id = 12, title = "name6"),
		createDefaultMovie(id = 13, title = "name6"),
	)

	fun createNowPlayingMovieList() = listOf(
		createDefaultMovie(id = 0, title = "name101", overview = "Overview 0"),
		createDefaultMovie(id = 1, title = "name102"),
		createDefaultMovie(id = 2, title = "name103", overview = "Overview 2"),
		createDefaultMovie(id = 3, title = "name104", overview = "Overview 3"),
		createDefaultMovie(id = 4, title = "name105"),
		createDefaultMovie(id = 5, title = "name106"),
		createDefaultMovie(id = 6, title = "name6"),
		createDefaultMovie(id = 7, title = "name6"),
		createDefaultMovie(id = 8, title = "name6"),
		createDefaultMovie(id = 9, title = "name6"),
		createDefaultMovie(id = 10, title = "name6"),
		createDefaultMovie(id = 11, title = "name6"),
		createDefaultMovie(id = 12, title = "name6"),
		createDefaultMovie(id = 13, title = "name6"),
	)

	fun createUpcomingMovieList() = listOf(
		createDefaultMovie(id = 0, title = "name201", overview = "Overview 0"),
		createDefaultMovie(id = 1, title = "name202"),
		createDefaultMovie(id = 2, title = "name203", overview = "Overview 2"),
		createDefaultMovie(id = 3, title = "name204", overview = "Overview 3"),
		createDefaultMovie(id = 4, title = "name205"),
		createDefaultMovie(id = 5, title = "name206"),
		createDefaultMovie(id = 6, title = "name6"),
		createDefaultMovie(id = 7, title = "name6"),
		createDefaultMovie(id = 8, title = "name6"),
		createDefaultMovie(id = 9, title = "name6"),
		createDefaultMovie(id = 10, title = "name6"),
		createDefaultMovie(id = 11, title = "name6"),
		createDefaultMovie(id = 12, title = "name6"),
		createDefaultMovie(id = 13, title = "name6"),
	)

	fun createMovieDetail(
		id: Int = 0,
		title: String = "Brazil",
		overview: String = "Best film ever",
		voteAverage: Float = 8.7800f,
		posterPath: String = "app1",
		backdropPath: String = ""
	): MovieDetail = MovieDetail(
		id = id,
		title = title,
		overview = overview,
		voteAverage = voteAverage,
		posterPath = posterPath,
		backdropPath = backdropPath,
		genres = listOf(
			Genre(1, "Adventure"), Genre(2, "Comedy")
		).toImmutableList()
	)

}

private fun createDefaultMovie(
	id: Int = 0,
	title: String = "Brazil",
	overview: String = "Best film ever",
	voteAverage: Float = 8.7800f,
	posterPath: String = "app1",
	backdropPath: String = ""
): Movie = Movie(
	id = id,
	title = title,
	overview = overview,
	voteAverage = voteAverage,
	posterPath = posterPath,
	backdropPath = backdropPath
)
