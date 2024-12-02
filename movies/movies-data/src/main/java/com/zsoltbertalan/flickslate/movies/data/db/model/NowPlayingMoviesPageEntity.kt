package com.zsoltbertalan.flickslate.movies.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zsoltbertalan.flickslate.shared.model.PageData

@Entity(tableName = "nowPlayingMoviesPages")
data class NowPlayingMoviesPageEntity(
	@PrimaryKey
	val page: Int,
	val date: String = "",
	val expires: Int = 0,
	val etag: String,
	val totalPages: Int = 0,
	val totalResults: Int = 0,
)

fun PageData.toNowPlayingMoviesPageEntity(): NowPlayingMoviesPageEntity = NowPlayingMoviesPageEntity(
	page, date, expires, etag, totalPages, totalResults,
)

fun NowPlayingMoviesPageEntity.toPageData(): PageData = PageData(
	page, date, expires, etag, totalPages, totalResults,
)
