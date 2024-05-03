package com.zsoltbertalan.flickslate.data.db

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class EtagDbo() : RealmObject {

	constructor(
		id: String,
		etag: String,
	) : this() {
		this.id = id
		this.etag = etag
	}

	@PrimaryKey
	var id: String = ""
	var etag: String = ""

}
