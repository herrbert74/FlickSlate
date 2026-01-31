package com.zsoltbertalan.flickslate.shared.domain.model

data class Account(
	val displayName: String,
	val username: String,
	val language: String,
	val region: String,
	val id: Int,
	val includeAdult: Boolean,
)
