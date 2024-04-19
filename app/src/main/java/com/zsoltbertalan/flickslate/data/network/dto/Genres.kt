package com.zsoltbertalan.flickslate.data.network.dto

import com.babestudios.base.data.mapNullInputList
import com.zsoltbertalan.flickslate.domain.model.Genre
import kotlinx.serialization.Serializable

@Serializable
data class Genres(
	val id: Int? = null,
	val name: String? = null,
)

fun Genres.toGenre():Genre = Genre(this.id, this.name)

fun List<Genres>?.toGenreList(): List<Genre> = mapNullInputList(this) { genre -> genre.toGenre() }
