package com.zsoltbertalan.flickslate.testhelper

import com.zsoltbertalan.flickslate.data.network.dto.MovieDto
import com.zsoltbertalan.flickslate.data.network.dto.MoviesResponseDto

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

}

private fun createDefaultMovieDto(
	id: Int = 0,
	title: String = "Heisenberg",
	overview: String = "Overview",
	voteAverage: Float = 0.7800f,
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
