package com.zsoltbertalan.flickslate.search.data.db.model

import androidx.room.Entity
import com.zsoltbertalan.flickslate.shared.domain.model.PageData

@Entity(tableName = "GenreMoviesPages", primaryKeys = ["genreId", "page"])
data class GenreMoviesPageEntity(
	val genreId: Int,
	val page: Int,
	val date: String = "",
	val expires: Int = 0,
	val etag: String,
	val totalPages: Int = 0,
	val totalResults: Int = 0,
)

fun PageData.toGenreMoviesPageEntity(genreId: Int): GenreMoviesPageEntity = GenreMoviesPageEntity(
	genreId, page, date, expires, etag, totalPages, totalResults,
)

fun GenreMoviesPageEntity.toPageData(): PageData = PageData(
	page,
	date,
	expires,
	etag,
	totalPages,
	totalResults,
)
