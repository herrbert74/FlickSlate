package com.zsoltbertalan.flickslate.tv.data.network.model

const val TOTAL_RESULTS = 300

const val TOTAL_PAGES = 3

object TvDtoMother {

	fun createTopRatedTvReplyDto() = TopRatedTvReplyDto(
		0,
		TOTAL_PAGES,
		TOTAL_RESULTS,
		listOf(
			createDefaultTvDto(),
			createDefaultTvDto(id = 1, name = "name2"),
			createDefaultTvDto(id = 2, name = "name3", overview = "Overview 2"),
			createDefaultTvDto(id = TOTAL_PAGES, name = "name4", overview = "Overview 3"),
			createDefaultTvDto(id = 4, name = "name5"),
			createDefaultTvDto(id = 5, name = "name6"),
			createDefaultTvDto(id = 6, name = "name6"),
			createDefaultTvDto(id = 7, name = "name6"),
			createDefaultTvDto(id = 8, name = "name6"),
			createDefaultTvDto(id = 9, name = "name6"),
			createDefaultTvDto(id = 10, name = "name6"),
			createDefaultTvDto(id = 11, name = "name6"),
			createDefaultTvDto(id = 12, name = "name6"),
		)
	)

	private fun createDefaultTvDto(
		id: Int = 0,
		name: String = "Detectorists",
		overview: String = """The lives of two eccentric metal detectorists, who spend their days plodding along 
			|ploughed tracks and open fields, hoping to disturb the tedium by unearthing the fortune of a lifetime.
			""".trimMargin(),
		voteAverage: Float = 8.8473f,
		posterPath: String = "/eclnU0b9BbvykXoXEd3CGAFwJUO.jpg",
		backdropPath: String = "/5cstdAjVuXJ66SFQZXpekSqXq6i.jpg",
	): TvDto = TvDto(
		id = id,
		name = name,
		overview = overview,
		vote_average = voteAverage,
		poster_path = posterPath,
		backdrop_path = backdropPath
	)

}
