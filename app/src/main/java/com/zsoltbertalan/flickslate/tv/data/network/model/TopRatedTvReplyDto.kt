package com.zsoltbertalan.flickslate.tv.data.network.model

import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import kotlinx.serialization.Serializable

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
data class TopRatedTvReplyDto(
	val page: Int? = null,
	val total_pages: Int? = null,
	val total_results: Int? = null,
	val results: List<TvDto>? = null,
)

fun TopRatedTvReplyDto.toTvList() =
	PagingReply(this.results?.toTvList()?: emptyList(), page == total_pages)
