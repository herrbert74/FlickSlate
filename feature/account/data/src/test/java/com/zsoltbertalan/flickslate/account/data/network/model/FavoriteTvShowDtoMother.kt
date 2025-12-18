package com.zsoltbertalan.flickslate.account.data.network.model

import com.zsoltbertalan.flickslate.shared.data.network.model.TvShowDto

internal object FavoriteTvShowDtoMother {

	fun createFavoriteTvShowDto(
		id: Int = 1,
		name: String = "Test Show",
	) = TvShowDto(
		id = id,
		name = name,
	)

}
