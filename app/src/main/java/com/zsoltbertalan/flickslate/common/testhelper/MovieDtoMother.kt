package com.zsoltbertalan.flickslate.common.testhelper

import com.zsoltbertalan.flickslate.data.network.dto.MovieDto
import com.zsoltbertalan.flickslate.data.network.dto.MoviesResponseDto
import com.zsoltbertalan.flickslate.data.network.dto.NowPlayingMoviesResponse
import com.zsoltbertalan.flickslate.data.network.dto.UpcomingMoviesResponse

object MovieDtoMother {

	fun createMoviesResponseDto() = MoviesResponseDto(
		0, 3, 300,
		listOf(
			createDefaultMovieDto(id = 0, title = "name1", overview = "Overview 0"),
			createDefaultMovieDto(id = 1, title = "name2"),
			createDefaultMovieDto(id = 2, title = "name3", overview = "Overview 2"),
			createDefaultMovieDto(id = 3, title = "name4", overview = "Overview 3"),
			createDefaultMovieDto(id = 4, title = "name5"),
			createDefaultMovieDto(id = 5, title = "name6"),
		)
	)

	fun createPopularMovieList() = MoviesResponseDto(
		0, 3, 300,
		listOf(
			createDefaultMovieDto(id = 0, title = "name101", overview = "Overview 0"),
			createDefaultMovieDto(id = 1, title = "name102"),
			createDefaultMovieDto(id = 2, title = "name103", overview = "Overview 2"),
			createDefaultMovieDto(id = 3, title = "name104", overview = "Overview 3"),
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

	fun createNowPlayingMovieList() = NowPlayingMoviesResponse(
		0, 3, 300,
		listOf(
			createDefaultMovieDto(id = 0, title = "name101", overview = "Overview 0"),
			createDefaultMovieDto(id = 1, title = "name102"),
			createDefaultMovieDto(id = 2, title = "name103", overview = "Overview 2"),
			createDefaultMovieDto(id = 3, title = "name104", overview = "Overview 3"),
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

	fun createUpcomingMovieList() = UpcomingMoviesResponse(
		0, 3, 300,
		listOf(
			createDefaultMovieDto(id = 0, title = "name201", overview = "Overview 0"),
			createDefaultMovieDto(id = 1, title = "name202"),
			createDefaultMovieDto(id = 2, title = "name203", overview = "Overview 2"),
			createDefaultMovieDto(id = 3, title = "name204", overview = "Overview 3"),
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
	title: String = "Heisenberg",
	overview: String = "Overview",
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
