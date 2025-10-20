package com.zsoltbertalan.flickslate.account.data.network.model

import com.zsoltbertalan.flickslate.shared.data.network.model.MovieDto
import kotlinx.serialization.Serializable

@Serializable
internal data class RatedMovieReplyDto(
    val results: List<MovieDto>
)
