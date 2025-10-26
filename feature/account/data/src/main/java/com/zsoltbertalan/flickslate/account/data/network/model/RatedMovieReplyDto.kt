package com.zsoltbertalan.flickslate.account.data.network.model

import kotlinx.serialization.Serializable

@Serializable
internal data class RatedMovieReplyDto(
    val results: List<RatedMovieDto>
)
