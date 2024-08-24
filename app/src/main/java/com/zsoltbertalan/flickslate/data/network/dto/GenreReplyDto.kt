package com.zsoltbertalan.flickslate.data.network.dto

import com.zsoltbertalan.flickslate.domain.model.Genre
import kotlinx.serialization.Serializable

@Serializable
data class GenreReplyDto(
	val genres: List<GenreDto> = emptyList(),
)

fun GenreReplyDto.toGenres(): List<Genre> = this.genres.toGenreList()
