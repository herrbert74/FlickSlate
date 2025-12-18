package com.zsoltbertalan.flickslate.tv.domain.model

import com.zsoltbertalan.flickslate.shared.domain.model.Genre
import com.zsoltbertalan.flickslate.shared.domain.model.images.ImagesReply
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class TvDetailWithImages(
	val id: Int? = null,
	val title: String? = null,
	val overview: String? = null,
	val voteAverage: Float? = null,
	val posterPath: String? = null,
	val backdropPath: String? = null,
	val genres: ImmutableList<Genre> = listOf<Genre>().toImmutableList(),
	val seasons: ImmutableList<Season> = listOf<Season>().toImmutableList(),
	val tvImages: ImagesReply = ImagesReply(),
	val status: String? = null,
	val tagline: String? = null,
	val years: String = "",
	val personalRating: Float = -1f,
	val favorite: Boolean = false,
)
