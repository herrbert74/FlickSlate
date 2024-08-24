package com.zsoltbertalan.flickslate.data.db

import com.zsoltbertalan.flickslate.domain.model.PageData
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class UpcomingMoviesPageDbo() : RealmObject {

	constructor(
		page: Int,
		date: String = "",
		expires: Int = 0,
		etag: String,
		totalPages: Int = 0,
		totalResults: Int = 0,
	) : this() {
		this.page = page
		this.date = date
		this.expires = expires
		this.etag = etag
		this.totalPages = totalPages
		this.totalResults = totalResults
	}

	@PrimaryKey
	var page: Int = 0
	var date: String = ""
	var expires: Int = 0
	var etag: String = ""
	var totalPages: Int = 0
	var totalResults: Int = 0

}

fun PageData.toUpcomingMoviesPageDbo(): UpcomingMoviesPageDbo = UpcomingMoviesPageDbo(
	page, date, expires, etag, totalPages, totalResults,
)

fun UpcomingMoviesPageDbo.toPageData(): PageData = PageData(
	page, date, expires, etag, totalPages, totalResults,
)
