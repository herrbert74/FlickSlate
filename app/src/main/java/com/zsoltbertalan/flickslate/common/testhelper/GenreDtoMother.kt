package com.zsoltbertalan.flickslate.common.testhelper

import com.zsoltbertalan.flickslate.data.network.dto.GenreDto

object GenreDtoMother {

	fun createGenreDtoList() =
		listOf(
			createDefaultGenreDto(id = 28, name = "Action"),
			createDefaultGenreDto(id = 12, name = "Adventure"),
			createDefaultGenreDto(id = 16, name = "Animation"),
			createDefaultGenreDto(id = 35, name = "Comedy"),
			createDefaultGenreDto(id = 80, name = "Crime"),
			createDefaultGenreDto(id = 99, name = "Documentary"),
			createDefaultGenreDto(id = 18, name = "Drama"),
			createDefaultGenreDto(id = 10751, name = "Family"),
		)

}

private fun createDefaultGenreDto(
	id: Int,
	name: String
) = GenreDto(id, name)
