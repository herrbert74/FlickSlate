package com.zsoltbertalan.flickslate.search.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "eTags")
data class EtagEntity(

	@PrimaryKey
	val id: String,

	val etag: String,
)
