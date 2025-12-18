package com.zsoltbertalan.flickslate.account.data.network.model

import com.zsoltbertalan.flickslate.shared.data.network.model.MovieDto

internal object FavoriteMovieDtoMother {

	fun createFavoriteMovieDto(
		id: Int = 1,
		title: String = "Test Movie",
	) = MovieDto(
		id = id,
		title = title,
	)

}
