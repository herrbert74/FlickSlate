package com.zsoltbertalan.flickslate.search.data.db.model

import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class GenreMoviesPageDbo() : RealmObject {

	constructor(
		genreIdAndPage: String,
		date: String = "",
		expires: Int = 0,
		etag: String,
		totalPages: Int = 0,
		totalResults: Int = 0,
	) : this() {
		this.genreIdAndPage = genreIdAndPage
		this.date = date
		this.expires = expires
		this.etag = etag
		this.totalPages = totalPages
		this.totalResults = totalResults
	}

	@PrimaryKey
	var genreIdAndPage: String = ""
	var date: String = ""
	var expires: Int = 0
	var etag: String = ""
	var totalPages: Int = 0
	var totalResults: Int = 0

}

fun PageData.toGenreMoviesPageDbo(genreId: Int): GenreMoviesPageDbo = GenreMoviesPageDbo(
	"${genreId}_${page}", date, expires, etag, totalPages, totalResults,
)

fun GenreMoviesPageDbo.toPageData(): PageData = PageData(
	genreIdAndPage.split("_").getOrElse(1) { "0" }.toInt(),
	date,
	expires,
	etag,
	totalPages,
	totalResults,
)
