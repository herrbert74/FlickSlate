package com.zsoltbertalan.flickslate.account.data.network.model

import com.zsoltbertalan.flickslate.shared.data.network.model.TvShowDto

internal object FavoriteTvShowReplyDtoMother {

	fun createFavoriteTvShowReplyDto(
		page: Int = 1,
		totalPages: Int = 2,
		totalResults: Int = 1,
		results: List<TvShowDto> = listOf(FavoriteTvShowDtoMother.createFavoriteTvShowDto()),
	) = FavoriteTvShowReplyDto(
		page = page,
		total_pages = totalPages,
		total_results = totalResults,
		results = results,
	)

}
