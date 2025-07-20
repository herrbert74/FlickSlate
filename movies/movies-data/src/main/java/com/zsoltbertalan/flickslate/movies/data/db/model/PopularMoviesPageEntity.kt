package com.zsoltbertalan.flickslate.movies.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zsoltbertalan.flickslate.shared.model.PageData

@Entity(tableName = "popularMoviesPages")
class PopularMoviesPageEntity(

	@PrimaryKey
	val page: Int,

	val date: String = "",
	val expires: Int = 0,
	val etag: String,
	val totalPages: Int = 0,
	val totalResults: Int = 0,
)

fun PageData.toPopularMoviesPageEntity(): PopularMoviesPageEntity =
	PopularMoviesPageEntity(page, date, expires, etag, totalPages, totalResults)

fun PopularMoviesPageEntity.toPageData(): PageData =
	PageData(page, date, expires, etag, totalPages, totalResults)
