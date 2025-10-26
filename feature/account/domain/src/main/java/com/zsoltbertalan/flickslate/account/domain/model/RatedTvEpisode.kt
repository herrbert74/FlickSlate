package com.zsoltbertalan.flickslate.account.domain.model

import com.zsoltbertalan.flickslate.shared.domain.model.TvEpisodeDetail

data class RatedTvEpisode(
	val tvEpisodeDetail: TvEpisodeDetail,
	val rating: Float
)
