package com.zsoltbertalan.flickslate.tv.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zsoltbertalan.flickslate.shared.domain.model.PageData

@Entity(tableName = "TvPages")
data class TvPageEntity(

	@PrimaryKey
	val page: Int,

	val date: String = "",
	val expires: Int = 0,
	val etag: String,
	val totalPages: Int = 0,
	val totalResults: Int = 0,
)

fun PageData.toTvPageEntity(): TvPageEntity = TvPageEntity(
	page, date, expires, etag, totalPages, totalResults,
)

fun TvPageEntity.toPageData(): PageData = PageData(
	page, date, expires, etag, totalPages, totalResults,
)
