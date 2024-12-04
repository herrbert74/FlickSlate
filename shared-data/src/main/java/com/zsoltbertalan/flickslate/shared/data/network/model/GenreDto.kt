package com.zsoltbertalan.flickslate.shared.data.network.model

import com.babestudios.base.data.mapNullInputList
import com.zsoltbertalan.flickslate.shared.model.Genre
import kotlinx.serialization.Serializable

@Serializable
data class GenreDto(
	val id: Int? = null,
	val name: String? = null,
)

fun GenreDto.toGenre(): Genre = Genre(this.id, this.name)

fun List<GenreDto>?.toGenresReply(): List<Genre> = mapNullInputList(this) { genre -> genre.toGenre() }