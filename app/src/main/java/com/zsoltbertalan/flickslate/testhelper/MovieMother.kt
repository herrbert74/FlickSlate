package com.zsoltbertalan.flickslate.testhelper

import com.zsoltbertalan.flickslate.domain.model.Movie

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
	)

}

private fun createDefaultMovie(
	id: Int = 0,
	title: String = "Heisenberg",
	overview: String = "Overview",
	voteAverage: Float = 0.7800f,
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
