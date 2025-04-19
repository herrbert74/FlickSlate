package com.zsoltbertalan.flickslate.movies.domain.model

import com.zsoltbertalan.flickslate.movies.domain.model.images.Image
import com.zsoltbertalan.flickslate.movies.domain.model.images.MovieImages
import com.zsoltbertalan.flickslate.shared.model.Genre
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class MovieDetailWithImages(
	val id: Int? = null,
	val title: String? = null,
	val overview: String? = null,
	val voteAverage: Float? = null,
	val posterPath: String? = null,
	val backdropPath: String? = null,
	val genres: ImmutableList<Genre> = listOf<Genre>().toImmutableList(),
	val movieImages: MovieImages = MovieImages(),
)
