package com.zsoltbertalan.flickslate.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
	val genres: List<Genres> = emptyList(),
)

fun GenreResponse.toGenres() = this.genres.toGenreList()
