package com.zsoltbertalan.flickslate.account.data.network.model

import com.zsoltbertalan.flickslate.account.domain.model.RatedMovie
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import kotlinx.serialization.Serializable

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
internal data class RatedMovieDto(
	val popularity: Float? = null,
	val vote_count: Int? = null,
	val video: Boolean? = null,
	val poster_path: String? = null,
	val id: Int? = null,
	val adult: Boolean? = null,
	val backdrop_path: String? = null,
	val original_language: String? = null,
	val original_title: String? = null,
	val genre_ids: List<Int>? = null,
	val title: String? = null,
	val vote_average: Float? = null,
	val overview: String? = null,
	val release_date: String? = null,
	val rating: Float
)

internal fun RatedMovieDto.toRatedMovie() = RatedMovie(
	movie = Movie(
		id = this.id ?: 0,
		title = this.title ?: "",
		overview = this.overview,
		voteAverage = this.vote_average,
		posterPath = this.poster_path,
		backdropPath = this.backdrop_path
	),
	rating = this.rating
)
