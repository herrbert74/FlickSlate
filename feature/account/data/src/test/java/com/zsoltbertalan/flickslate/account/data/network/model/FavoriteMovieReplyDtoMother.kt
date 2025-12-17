package com.zsoltbertalan.flickslate.account.data.network.model

import com.zsoltbertalan.flickslate.shared.data.network.model.MovieDto

internal object FavoriteMovieReplyDtoMother {

	fun createFavoriteMovieReplyDto(
		page: Int = 1,
		totalPages: Int = 2,
		totalResults: Int = 1,
		results: List<MovieDto> = listOf(FavoriteMovieDtoMother.createFavoriteMovieDto()),
	) = FavoriteMovieReplyDto(
		page = page,
		total_pages = totalPages,
		total_results = totalResults,
		results = results,
	)

}
