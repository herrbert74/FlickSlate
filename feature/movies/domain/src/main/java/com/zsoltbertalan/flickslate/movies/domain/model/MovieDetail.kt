package com.zsoltbertalan.flickslate.movies.domain.model

import com.zsoltbertalan.flickslate.shared.domain.model.Genre
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class MovieDetail(

	val id: Int? = null,

	val title: String? = null,

	val overview: String? = null,

	val voteAverage: Float? = null,

	val posterPath: String? = null,

	val backdropPath: String? = null,

	val genres: ImmutableList<Genre> = listOf<Genre>().toImmutableList(),

	val personalRating: Float = -1f,

	val favorite: Boolean = false,

	val watchlist: Boolean = false,

	)
