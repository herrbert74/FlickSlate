package com.zsoltbertalan.loccounter

data class LocResults(
	val modules: Map<String, List<CodeLines>>
)

enum class Extensions(val extension: String) {
	Kotlin("kt"),
}

data class CodeLines(
	val extension: String,
	val lines: Int
)
