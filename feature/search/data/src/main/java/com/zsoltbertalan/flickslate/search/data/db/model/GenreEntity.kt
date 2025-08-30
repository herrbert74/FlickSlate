package com.zsoltbertalan.flickslate.search.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zsoltbertalan.flickslate.shared.domain.model.Genre

@Entity(tableName = "genres")
internal data class GenreEntity(

	@PrimaryKey
	val id: Int,

	val name: String,
)

internal fun Genre.toEntity(): GenreEntity = GenreEntity(
	id = this.id ?: 0,
	name = this.name ?: "",
)

internal fun GenreEntity.toGenre(): Genre = Genre(
	id = this.id,
	name = this.name,
)
