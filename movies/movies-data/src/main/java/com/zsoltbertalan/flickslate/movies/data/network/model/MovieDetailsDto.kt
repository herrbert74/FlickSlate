package com.zsoltbertalan.flickslate.movies.data.network.model

import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetail
import com.zsoltbertalan.flickslate.shared.data.network.model.GenreDto
import com.zsoltbertalan.flickslate.shared.data.network.model.toGenresReply
import kotlinx.serialization.Serializable

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
data class MovieDetailsDto(
	val popularity: Float? = null,
	val vote_count: Int? = null,
	val video: Boolean? = null,
	val poster_path: String? = null,
	val id: Int? = null,
	val adult: Boolean? = null,
	val backdrop_path: String? = null,
	val original_language: String? = null,
	val original_title: String? = null,
	val title: String? = null,
	val genres: List<GenreDto>? = null,
	val vote_average: Float? = null,
	val overview: String? = null,
	val release_date: String? = null,
)

fun MovieDetailsDto.toMovieDetail() = MovieDetail(
	this.id,
	this.title,
	this.overview,
	this.vote_average,
	this.poster_path,
	this.backdrop_path,
	this.genres.toGenresReply(),
)
