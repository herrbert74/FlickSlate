package com.zsoltbertalan.flickslate.testhelper

import com.zsoltbertalan.flickslate.shared.data.network.model.MovieDto
import com.zsoltbertalan.flickslate.shared.data.network.model.MoviesReplyDto
import com.zsoltbertalan.flickslate.movies.data.network.model.NowPlayingMoviesReplyDto
import com.zsoltbertalan.flickslate.movies.data.network.model.UpcomingMoviesReplyDto

private const val TOTAL_RESULTS = 300

private const val TOTAL_PAGES = 3

object MovieDtoMother {

	fun createMoviesResponseDto() = MoviesReplyDto(
		0, TOTAL_PAGES, TOTAL_RESULTS,
		listOf(
			createDefaultMovieDto(id = 0, title = "name1", overview = "Overview 0"),
			createDefaultMovieDto(id = 1, title = "name2"),
			createDefaultMovieDto(id = 2, title = "name3", overview = "Overview 2"),
			createDefaultMovieDto(id = TOTAL_PAGES, title = "name4", overview = "Overview 3"),
			createDefaultMovieDto(id = 4, title = "name5"),
			createDefaultMovieDto(id = 5, title = "name6"),
		)
	)

	fun createPopularMovieList() = MoviesReplyDto(
		0, TOTAL_PAGES, TOTAL_RESULTS,
		listOf(
			createDefaultMovieDto(id = 0, title = "name101", overview = "Overview 0"),
			createDefaultMovieDto(id = 1, title = "name102"),
			createDefaultMovieDto(id = 2, title = "name103", overview = "Overview 2"),
			createDefaultMovieDto(id = TOTAL_PAGES, title = "name104", overview = "Overview 3"),
			createDefaultMovieDto(id = 4, title = "name105"),
			createDefaultMovieDto(id = 5, title = "name106"),
			createDefaultMovieDto(id = 6, title = "name6"),
			createDefaultMovieDto(id = 7, title = "name6"),
			createDefaultMovieDto(id = 8, title = "name6"),
			createDefaultMovieDto(id = 9, title = "name6"),
			createDefaultMovieDto(id = 10, title = "name6"),
			createDefaultMovieDto(id = 11, title = "name6"),
			createDefaultMovieDto(id = 12, title = "name6"),
			createDefaultMovieDto(id = 13, title = "name6"),
		)
	)

	fun createNowPlayingMovieList() = NowPlayingMoviesReplyDto(
		0, TOTAL_PAGES, TOTAL_RESULTS,
		listOf(
			createDefaultMovieDto(id = 0, title = "name101", overview = "Overview 0"),
			createDefaultMovieDto(id = 1, title = "name102"),
			createDefaultMovieDto(id = 2, title = "name103", overview = "Overview 2"),
			createDefaultMovieDto(id = TOTAL_PAGES, title = "name104", overview = "Overview 3"),
			createDefaultMovieDto(id = 4, title = "name105"),
			createDefaultMovieDto(id = 5, title = "name106"),
			createDefaultMovieDto(id = 6, title = "name6"),
			createDefaultMovieDto(id = 7, title = "name6"),
			createDefaultMovieDto(id = 8, title = "name6"),
			createDefaultMovieDto(id = 9, title = "name6"),
			createDefaultMovieDto(id = 10, title = "name6"),
			createDefaultMovieDto(id = 11, title = "name6"),
			createDefaultMovieDto(id = 12, title = "name6"),
			createDefaultMovieDto(id = 13, title = "name6"),
		)
	)

	fun createUpcomingMovieList() = UpcomingMoviesReplyDto(
		0, TOTAL_PAGES, TOTAL_RESULTS,
		listOf(
			createDefaultMovieDto(id = 0, title = "name201", overview = "Overview 0"),
			createDefaultMovieDto(id = 1, title = "name202"),
			createDefaultMovieDto(id = 2, title = "name203", overview = "Overview 2"),
			createDefaultMovieDto(id = TOTAL_PAGES, title = "name204", overview = "Overview 3"),
			createDefaultMovieDto(id = 4, title = "name205"),
			createDefaultMovieDto(id = 5, title = "name206"),
			createDefaultMovieDto(id = 6, title = "name6"),
			createDefaultMovieDto(id = 7, title = "name6"),
			createDefaultMovieDto(id = 8, title = "name6"),
			createDefaultMovieDto(id = 9, title = "name6"),
			createDefaultMovieDto(id = 10, title = "name6"),
			createDefaultMovieDto(id = 11, title = "name6"),
			createDefaultMovieDto(id = 12, title = "name6"),
			createDefaultMovieDto(id = 13, title = "name6"),
		)
	)
}

private fun createDefaultMovieDto(
	id: Int = 0,
	title: String = "Brazil",
	overview: String = "Best film ever",
	voteAverage: Float = 8.7800f,
	posterPath: String = "app1",
	backdropPath: String = ""
): MovieDto = MovieDto(
	id = id,
	title = title,
	overview = overview,
	vote_average = voteAverage,
	poster_path = posterPath,
	backdrop_path = backdropPath
)
