package com.zsoltbertalan.flickslate.data.network.dto

import com.zsoltbertalan.flickslate.domain.model.PagingReply
import kotlinx.serialization.Serializable

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
data class NowPlayingMoviesReplyDto(
	val page: Int? = null,
	val total_pages: Int? = null,
	val total_results: Int? = null,
	val results: List<MovieDto>? = null,
	val dates: DatesDto? = null,
)

fun NowPlayingMoviesReplyDto.toMoviesResponse() =
	PagingReply(this.results?.toMovieList()?: emptyList(), page == total_pages)
