package com.zsoltbertalan.flickslate.data.network.dto

import com.zsoltbertalan.flickslate.domain.model.Genre
import kotlinx.serialization.Serializable

@Serializable
data class GenreReply(
	val genres: List<Genres> = emptyList(),
)

fun GenreReply.toGenres(): List<Genre> = this.genres.toGenreList()
