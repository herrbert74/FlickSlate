package com.zsoltbertalan.flickslate.data.network.dto

import kotlinx.serialization.Serializable

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
data class TopRatedTvResponse(
	val page: Int? = null,
	val total_pages: Int? = null,
	val total_results: Int? = null,
	val results: List<TvDto>? = null,
)

fun TopRatedTvResponse.toTvList() = this.results?.toTvList()?: emptyList()
