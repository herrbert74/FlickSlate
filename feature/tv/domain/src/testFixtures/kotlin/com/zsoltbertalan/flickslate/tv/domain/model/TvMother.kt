package com.zsoltbertalan.flickslate.tv.domain.model

import com.zsoltbertalan.flickslate.shared.domain.model.Genre
import com.zsoltbertalan.flickslate.shared.domain.model.TvEpisodeDetail
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import com.zsoltbertalan.flickslate.shared.domain.model.images.Image
import com.zsoltbertalan.flickslate.shared.domain.model.images.ImagesReply
import kotlinx.collections.immutable.toImmutableList

/**
 * This is an example of an ObjectMother that can be used in both Unit and Android UI tests.
 * As such it would go into its own module in a normal project.
 */
object TvMother {

	fun createTvList() = listOf(
		createDefaultTv(id = 0, name = "Detectorists"),
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
	)

	private fun createDefaultTv(
		id: Int = 0,
		name: String = "Detectorists",
		overview: String = """The lives of two eccentric metal detectorists, who spend their days plodding along 
			|ploughed tracks and open fields, hoping to disturb the tedium by unearthing the fortune of a lifetime.
			""".trimMargin(),
		voteAverage: Float = 8.8473f,
		posterPath: String = "/eclnU0b9BbvykXoXEd3CGAFwJUO.jpg",
		backdropPath: String = "/5cstdAjVuXJ66SFQZXpekSqXq6i.jpg",
	): TvShow = TvShow(
		id = id,
		name = name,
		overview = overview,
		voteAverage = voteAverage,
		posterPath = posterPath,
		backdropPath = backdropPath,
	)

	fun createTvDetail(
		id: Int = 0,
		title: String = "Detectorists",
		overview: String = """The lives of two eccentric metal detectorists, who spend their days plodding along 
			|ploughed tracks and open fields, hoping to disturb the tedium by unearthing the fortune of a lifetime.
		""".trimMargin(),
		voteAverage: Float = 8.8473f,
		posterPath: String = "/eclnU0b9BbvykXoXEd3CGAFwJUO.jpg",
		backdropPath: String = "/5cstdAjVuXJ66SFQZXpekSqXq6i.jpg",
		tagline: String = "Tagline",
		personalRating: Float = -1f,
	): TvDetail = TvDetail(
		id = id,
		title = title,
		overview = overview,
		voteAverage = voteAverage,
		posterPath = posterPath,
		backdropPath = backdropPath,
		seasons = listOf(
			Season(
				"2014-04-30",
				episodeCount = 10,
				id = 0,
				name = "Season 1",
				overview = "",
				posterPath = "",
				seasonNumber = 1
			),
		).toImmutableList(),
		genres = listOf(
			Genre(1, "Adventure"),
			Genre(2, "Comedy")
		).toImmutableList(),
		tagline = tagline,
		personalRating = personalRating,
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

	fun createTvDetailWithImages(
		id: Int = 0,
		title: String = "Detectorists",
		overview: String = """The lives of two eccentric metal detectorists, who spend their days plodding along 
			|ploughed tracks and open fields, hoping to disturb the tedium by unearthing the fortune of a lifetime.
		""".trimMargin(),
		voteAverage: Float = 8.8473f,
		posterPath: String = "/eclnU0b9BbvykXoXEd3CGAFwJUO.jpg",
		backdropPath: String = "/5cstdAjVuXJ66SFQZXpekSqXq6i.jpg",
		tagline: String = "Tagline",
		personalRating: Float = -1f,
	): TvDetailWithImages = TvDetailWithImages(
		id = id,
		title = title,
		overview = overview,
		voteAverage = voteAverage,
		posterPath = posterPath,
		backdropPath = backdropPath,
		seasons = listOf(
			Season(
				"2014-04-30",
				episodeCount = 10,
				id = 0,
				name = "Season 1",
				overview = "",
				posterPath = "",
				seasonNumber = 1
			),
		).toImmutableList(),
		genres = listOf(
			Genre(1, "Adventure"),
			Genre(2, "Comedy")
		).toImmutableList(),
		tagline = tagline,
		personalRating = personalRating,
		tvImages = createTvImages()
	)

	fun createSeasonDetail(
		seriesId: Int,
		seasonNumber: Int
	) = SeasonDetail(
		id = seriesId,
		seasonNumber = seasonNumber,
		episodes = listOf(createTvEpisodeDetail()),
		airDate = "",
		episodeCount = 10,
		name = "",
		overview = "",
		posterPath = "",
		voteAverage = 6.7f
	)

	fun createTvEpisodeDetail(): TvEpisodeDetail =
		TvEpisodeDetail(
			id = 0,
			showId = 1,
			name = "Episode 1",
			overview = "Overview ",
			seasonNumber = 1,
			episodeNumber = 1,
			stillPath = "/5cstdAjVuXJ66SFQZXpekSqXq6i.jpg",
			airDate = "20",
			voteAverage = 6.7f,
			voteCount = 3321,
			personalRating = 6f
		)

}
