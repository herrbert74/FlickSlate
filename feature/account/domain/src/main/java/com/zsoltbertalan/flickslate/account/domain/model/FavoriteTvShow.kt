package com.zsoltbertalan.flickslate.account.domain.model

import com.zsoltbertalan.flickslate.shared.domain.model.TvShow

/** Domain model representing a TV show that is in the user's favorites list. */
data class FavoriteTvShow(
	val tvShow: TvShow,
)
