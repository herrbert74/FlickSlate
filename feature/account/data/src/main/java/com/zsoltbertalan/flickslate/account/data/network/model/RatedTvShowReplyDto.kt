package com.zsoltbertalan.flickslate.account.data.network.model

import com.zsoltbertalan.flickslate.shared.data.network.model.TvShowDto
import kotlinx.serialization.Serializable

@Serializable
internal data class RatedTvShowReplyDto(
    val results: List<TvShowDto>
)
