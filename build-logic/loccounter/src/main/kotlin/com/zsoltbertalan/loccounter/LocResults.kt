package com.zsoltbertalan.loccounter

import kotlinx.serialization.Serializable

@Serializable
data class LocResults(
	val totalKotlin: Int,
	val totalJava: Int,
	val modules: Map<String, Pair<Int, Int>>
)
