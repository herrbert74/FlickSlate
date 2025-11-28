package com.zsoltbertalan.flickslate.tv.data.network.model.images

import com.zsoltbertalan.flickslate.shared.data.network.model.images.ImageDto
import com.zsoltbertalan.flickslate.shared.data.network.model.images.ImagesReplyDto

internal object ImagesReplyDtoMother {

	fun createImagesReplyDto(): ImagesReplyDto {
		return ImagesReplyDto(
			backdrops = listOf(createImageDto("/backdrop.jpg")),
			logos = emptyList(),
			posters = listOf(createImageDto("/poster.jpg")),
			id = 1
		)
	}

	private fun createImageDto(path: String) = ImageDto(
		aspect_ratio = 1.78,
		file_path = path,
		height = 1080,
		iso_639_1 = "en",
		vote_average = 5.0,
		vote_count = 10,
		width = 1920
	)
}
