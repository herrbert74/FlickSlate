package com.zsoltbertalan.profiler

import kotlinx.serialization.Serializable

@Serializable
data class ProfilerResult(
	val summary: String,
	val results: Map<String, String>
)
