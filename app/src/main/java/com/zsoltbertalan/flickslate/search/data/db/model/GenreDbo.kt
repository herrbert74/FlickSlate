package com.zsoltbertalan.flickslate.search.data.db.model

import com.zsoltbertalan.flickslate.shared.domain.model.Genre
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class GenreDbo() : RealmObject {

	constructor(
		id: Int,
		name: String,
	) : this() {
		this.id = id
		this.name = name
	}

	@PrimaryKey
	var id: Int = 0
	var name: String = ""

}

fun Genre.toDbo(): GenreDbo = GenreDbo(
	id = this.id ?: 0,
	name = this.name ?: "",
)

fun GenreDbo.toGenre(): Genre = Genre(
	id = this.id,
	name = this.name,
)
