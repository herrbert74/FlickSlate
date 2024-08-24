package com.zsoltbertalan.flickslate.data.network.dto

import com.zsoltbertalan.flickslate.domain.model.PagingReply
import kotlinx.serialization.Serializable

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
data class TopRatedTvResponse(
	val page: Int? = null,
	val total_pages: Int? = null,
	val total_results: Int? = null,
	val results: List<TvDto>? = null,
)

fun TopRatedTvResponse.toTvList() =
	PagingReply(this.results?.toTvList()?: emptyList(), page == total_pages)
