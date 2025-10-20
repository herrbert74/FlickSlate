package com.zsoltbertalan.flickslate.account.data.network.model

import com.zsoltbertalan.flickslate.shared.data.network.model.TvEpisodeDetailsDto
import kotlinx.serialization.Serializable

@Serializable
internal data class RatedTvShowEpisodeReplyDto(
    val results: List<TvEpisodeDetailsDto>
)
