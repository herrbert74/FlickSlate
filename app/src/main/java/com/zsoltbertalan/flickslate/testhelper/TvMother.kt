package com.zsoltbertalan.flickslate.testhelper

import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetail
import com.zsoltbertalan.flickslate.shared.model.Genre
import com.zsoltbertalan.flickslate.tv.domain.model.TvDetail
import com.zsoltbertalan.flickslate.tv.domain.model.TvShow
import kotlinx.collections.immutable.toImmutableList

/**
 * This is an example of an ObjectMother that can be used in both Unit and Android UI tests.
 * As such it would go into its own module in a normal project.
 */
object TvMother {

	fun createTvList() = listOf(
		createDefaultTv(id = 0, name = "name1", overview = "Overview 0"),
		createDefaultTv(id = 1, name = "name2"),
		createDefaultTv(id = 2, name = "name3", overview = "Overview 2"),
		createDefaultTv(id = 3, name = "name4", overview = "Overview 3"),
		createDefaultTv(id = 4, name = "name5"),
		createDefaultTv(id = 5, name = "name6"),
		createDefaultTv(id = 6, name = "name6"),
		createDefaultTv(id = 7, name = "name6"),
		createDefaultTv(id = 8, name = "name6"),
		createDefaultTv(id = 9, name = "name6"),
		createDefaultTv(id = 10, name = "name6"),
		createDefaultTv(id = 11, name = "name6"),
		createDefaultTv(id = 12, name = "name6"),
		createDefaultTv(id = 12, name = "name6"),
	)


	private fun createDefaultTv(
		id: Int = 0,
		name: String = "Heisenberg",
		overview: String = "Overview",
		voteAverage: Float = 0.7800f,
		posterPath: String = "app1",
		backdropPath: String = ""
	): TvShow = TvShow(
		id = id,
		name = name,
		overview = overview,
		voteAverage = voteAverage,
		posterPath = posterPath,
		backdropPath = backdropPath
	)

	fun createTvDetail(
		id: Int = 0,
		title: String = "Brazil",
		overview: String = "Best film ever",
		voteAverage: Float = 8.7800f,
		posterPath: String = "app1",
		backdropPath: String = ""
	): TvDetail = TvDetail(
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

