package com.zsoltbertalan.profiler

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfilerNewRelicPayload(
	val common: ProfilerCommonBlock,
	val metrics: List<ProfilerMetric>
)

@Serializable
data class ProfilerCommonBlock(
	val attributes: ProfilerCommonAttributes
)

@Serializable
data class ProfilerCommonAttributes(
	@SerialName("service.name") val serviceName: String,
	val host: String
)

@Serializable
data class ProfilerMetric(
	val name: String,
	val type: String = "gauge",
	val value: Double,
	val timestamp: Long,
	val attributes: Map<String, String>? = null
)
