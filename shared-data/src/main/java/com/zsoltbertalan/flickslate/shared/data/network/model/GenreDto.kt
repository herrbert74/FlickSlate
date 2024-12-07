package com.zsoltbertalan.flickslate.shared.data.network.model

import com.zsoltbertalan.flickslate.shared.data.util.mapImmutableNullInputList
import com.zsoltbertalan.flickslate.shared.model.Genre
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.Serializable

@Serializable
data class GenreDto(
	val id: Int? = null,
	val name: String? = null,
)

fun GenreDto.toGenre(): Genre = Genre(this.id, this.name)

fun List<GenreDto>?.toGenresReply(): ImmutableList<Genre> = mapImmutableNullInputList(this) { genre -> genre.toGenre() }
