package com.zsoltbertalan.flickslate.data.network.dto

import com.zsoltbertalan.flickslate.domain.model.MoviesResponse
import kotlinx.serialization.Serializable

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
data class NowPlayingMoviesResponse(
	val page: Int? = null,
	val total_pages: Int? = null,
	val total_results: Int? = null,
	val results: List<MovieDto>? = null,
	val dates: Dates? = null,
)

fun NowPlayingMoviesResponse.toMoviesResponse() = MoviesResponse(this.results?.toMovieList()?: emptyList())
