package com.zsoltbertalan.flickslate.shared.data.network.model.images

import com.zsoltbertalan.flickslate.shared.domain.model.images.Image
import kotlinx.serialization.Serializable

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
data class ImageDto(
	val aspect_ratio: Double,
	val file_path: String,
	val height: Int,
	val iso_639_1: String?,
	val vote_average: Double,
	val vote_count: Int,
	val width: Int
)

fun ImageDto.toImage() = Image(
	this.aspect_ratio,
	this.file_path,
	this.height,
	this.iso_639_1,
	this.vote_average,
	vote_count,
	width
)
