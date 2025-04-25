package com.zsoltbertalan.flickslate.tv.domain.model

import com.zsoltbertalan.flickslate.shared.model.Genre
import com.zsoltbertalan.flickslate.shared.model.images.Image
import com.zsoltbertalan.flickslate.shared.model.images.ImagesReply
import kotlinx.collections.immutable.toImmutableList

/**
 * This is an example of an ObjectMother that can be used in both Unit and Android UI tests.
 * As such it would go into its own module in a normal project.
 */
object TvMother {

	fun createTvList() = listOf(
		createDefaultTv(id = 0, name = "Detectorists", overview = "Overview 0"),
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
		name: String = "Detectorists",
		overview: String = """The lives of two eccentric metal detectorists, who spend their days plodding along 
			|ploughed tracks and open fields, hoping to disturb the tedium by unearthing the fortune of a lifetime.
			|""".trimMargin(),
		voteAverage: Float = 8.8473f,
		posterPath: String = "/eclnU0b9BbvykXoXEd3CGAFwJUO.jpg",
		backdropPath: String = "/5cstdAjVuXJ66SFQZXpekSqXq6i.jpg"
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
		title: String = "Detectorists",
		overview: String = """The lives of two eccentric metal detectorists, who spend their days plodding along 
			|ploughed tracks and open fields, hoping to disturb the tedium by unearthing the fortune of a lifetime.
			|""".trimMargin(),
		voteAverage: Float = 8.8473f,
		posterPath: String = "/eclnU0b9BbvykXoXEd3CGAFwJUO.jpg",
		backdropPath: String = "/5cstdAjVuXJ66SFQZXpekSqXq6i.jpg"
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

	fun createTvImages(): ImagesReply = ImagesReply(
		backdrops = listOf(createDefaultImage()),
		posters = listOf(),
		logos = listOf(),
		id = 0
	)

	private fun createDefaultImage(): Image = Image(
		aspectRatio = 0.0,
		filePath = "/5cstdAjVuXJ66SFQZXpekSqXq6i.jpg",
		height = 2160,
		iso639dash1 = "",
		voteAverage = 0.0,
		voteCount = 0,
		width = 3840
	)

}

