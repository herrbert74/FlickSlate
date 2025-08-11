package com.zsoltbertalan.flickslate.shared.data.network.model.images

import com.zsoltbertalan.flickslate.shared.domain.model.images.ImagesReply
import kotlinx.serialization.Serializable

@Serializable
data class ImagesReplyDto(
	val backdrops: List<ImageDto>,
	val logos: List<ImageDto>,
	val posters: List<ImageDto>,
	val id: Int
)

fun ImagesReplyDto.toImagesReply() = ImagesReply(
	this.backdrops.map { it.toImage() },
	this.logos.map { it.toImage() },
	this.posters.map { it.toImage() },
	this.id
)
