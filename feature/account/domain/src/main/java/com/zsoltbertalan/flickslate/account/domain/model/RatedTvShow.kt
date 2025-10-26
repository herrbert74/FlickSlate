package com.zsoltbertalan.flickslate.account.domain.model

import com.zsoltbertalan.flickslate.shared.domain.model.TvShow

data class RatedTvShow(
	val tvShow: TvShow,
	val rating: Float
)
