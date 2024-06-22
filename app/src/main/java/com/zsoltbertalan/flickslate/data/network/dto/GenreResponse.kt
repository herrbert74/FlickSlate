package com.zsoltbertalan.flickslate.data.network.dto

import com.zsoltbertalan.flickslate.domain.model.Genre
import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
	val genres: List<Genres> = emptyList(),
)

fun GenreResponse.toGenres(): List<Genre> = this.genres.toGenreList()
