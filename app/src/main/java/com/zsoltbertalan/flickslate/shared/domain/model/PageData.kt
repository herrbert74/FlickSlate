package com.zsoltbertalan.flickslate.shared.domain.model

data class PageData(
	val page: Int = 0,
	val date: String = "",
	val expires: Int = 0,
	val etag: String = "",
	val totalPages: Int = 0,
	val totalResults: Int = 0,
)
