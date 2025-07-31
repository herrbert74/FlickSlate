package com.zsoltbertalan.flickslate.tv.domain.model

import com.zsoltbertalan.flickslate.shared.model.Genre
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class TvDetail(
	val id: Int? = null,
	val title: String? = null,
	val overview: String? = null,
	val voteAverage: Float? = null,
	val posterPath: String? = null,
	val backdropPath: String? = null,
	val genres: ImmutableList<Genre> = listOf<Genre>().toImmutableList(),
	val seasons: ImmutableList<Season> = listOf<Season>().toImmutableList(),
)
