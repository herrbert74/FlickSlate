package com.zsoltbertalan.flickslate.shared.data.network.model

import com.babestudios.base.data.mapNullInputList
import com.zsoltbertalan.flickslate.shared.model.Movie
import kotlinx.serialization.Serializable

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
data class MovieDto(
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
	val release_date: String? = null
)

fun List<MovieDto>.toMovieList(): List<Movie> = mapNullInputList(this) { movieDto -> movieDto.toMovie() }

fun MovieDto.toMovie() = Movie(
	this.id ?: 0,
	this.title ?: "",
	this.overview,
	this.vote_average,
	this.poster_path,
	this.backdrop_path
)
