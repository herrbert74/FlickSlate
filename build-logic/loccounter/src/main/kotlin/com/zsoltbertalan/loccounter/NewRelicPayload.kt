package com.zsoltbertalan.loccounter

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewRelicPayload(
	val common: CommonBlock,
	val metrics: List<Metric>
)

@Serializable
data class CommonBlock(
	val attributes: CommonAttributes
)

@Serializable
data class CommonAttributes(
	@SerialName("service.name") val serviceName: String,
	val host: String
)

@Serializable
data class Metric(
	val name: String,
	val type: String = "gauge",
	val value: Int,
	val timestamp: Long,
	val attributes: MetricAttributes? = null
)

@Serializable
data class MetricAttributes(
	val module: String
)