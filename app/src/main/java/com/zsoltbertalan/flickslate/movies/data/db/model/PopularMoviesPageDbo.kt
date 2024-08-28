package com.zsoltbertalan.flickslate.movies.data.db.model

import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class PopularMoviesPageDbo() : RealmObject {

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

fun PageData.toPopularMoviesPageDbo(): PopularMoviesPageDbo = PopularMoviesPageDbo(
	page, date, expires, etag, totalPages, totalResults,
)

fun PopularMoviesPageDbo.toPageData(): PageData = PageData(
	page, date, expires, etag, totalPages, totalResults,
)