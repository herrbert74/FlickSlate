package com.zsoltbertalan.flickslate.shared.domain.model

object GenreMother {

	fun createGenreList() =
		listOf(
			createDefaultGenre(id = 28, name = "Action"),
			createDefaultGenre(id = 12, name = "Adventure"),
			createDefaultGenre(id = 16, name = "Animation"),
			createDefaultGenre(id = 35, name = "Comedy"),
			createDefaultGenre(id = 80, name = "Crime"),
			createDefaultGenre(id = 99, name = "Documentary"),
			createDefaultGenre(id = 18, name = "Drama"),
			createDefaultGenre(id = 10751, name = "Family"),
		)

}

private fun createDefaultGenre(
	id: Int,
	name: String
) = Genre(id, name)
