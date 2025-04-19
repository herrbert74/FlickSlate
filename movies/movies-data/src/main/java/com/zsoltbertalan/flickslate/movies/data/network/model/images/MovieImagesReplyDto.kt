package com.zsoltbertalan.flickslate.movies.data.network.model.images

import com.zsoltbertalan.flickslate.movies.domain.model.images.MovieImages
import kotlinx.serialization.Serializable

@Serializable
data class MovieImagesReplyDto(
	val backdrops: List<ImageDto>,
	val logos: List<ImageDto>,
	val posters: List<ImageDto>,
	val id: Int
)

fun MovieImagesReplyDto.toMovieImages() = MovieImages(
	this.backdrops.map { it.toImage() },
	this.logos.map { it.toImage() },
	this.posters.map { it.toImage() },
	this.id
)
