package com.zsoltbertalan.flickslate.movies.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zsoltbertalan.flickslate.shared.domain.model.PageData

@Entity(tableName = "upcomingMoviesPages")
internal class UpcomingMoviesPageEntity(
	@PrimaryKey
	val page: Int,
	val date: String = "",
	val expires: Int = 0,
	val etag: String,
	val totalPages: Int = 0,
	val totalResults: Int = 0,
)

internal fun PageData.toUpcomingMoviesPageEntity(): UpcomingMoviesPageEntity =
	UpcomingMoviesPageEntity(page, date, expires, etag, totalPages, totalResults)

internal fun UpcomingMoviesPageEntity.toPageData(): PageData =
	PageData(page, date, expires, etag, totalPages, totalResults)
