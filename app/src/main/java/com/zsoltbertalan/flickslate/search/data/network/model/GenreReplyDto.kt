package com.zsoltbertalan.flickslate.search.data.network.model

import com.zsoltbertalan.flickslate.shared.domain.model.Genre
import kotlinx.serialization.Serializable

@Serializable
data class GenreReplyDto(
	val genres: List<GenreDto> = emptyList(),
)

fun GenreReplyDto.toGenres(): List<Genre> = this.genres.toGenreList()
