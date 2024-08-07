package com.zsoltbertalan.flickslate.data.network.dto

import com.zsoltbertalan.flickslate.domain.model.MoviesResponse
import kotlinx.serialization.Serializable

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
data class MoviesResponseDto(
	val page: Int? = null,
	val total_pages: Int? = null,
	val total_results: Int? = null,
	val results: List<MovieDto>? = null,
)

fun MoviesResponseDto.toMoviesResponse() = MoviesResponse(this.results?.toMovieList()?: emptyList())
