package com.zsoltbertalan.flickslate.data.network.dto

import com.babestudios.base.data.mapNullInputList
import com.zsoltbertalan.flickslate.domain.model.Genre
import kotlinx.serialization.Serializable

@Serializable
data class GenreDto(
	val id: Int? = null,
	val name: String? = null,
)

fun GenreDto.toGenre():Genre = Genre(this.id, this.name)

fun List<GenreDto>?.toGenreList(): List<Genre> = mapNullInputList(this) { genre -> genre.toGenre() }
