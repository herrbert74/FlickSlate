package com.zsoltbertalan.flickslate.shared.domain.model

data class PageData(
	val page: Int,
	val date: String,
	val expires: Int,
	val etag: String,
	val totalPages: Int,
	val totalResults: Int,
)
