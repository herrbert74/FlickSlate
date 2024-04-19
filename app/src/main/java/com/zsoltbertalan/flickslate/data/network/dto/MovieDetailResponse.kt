package com.zsoltbertalan.flickslate.data.network.dto

import com.zsoltbertalan.flickslate.domain.model.MovieDetail
import kotlinx.serialization.Serializable

@Suppress("PropertyName")
@Serializable
data class MovieDetailResponse(
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
	val genres: List<Genres>? = null,
	val vote_average: Float? = null,
	val overview: String? = null,
	val release_date: String? = null,
)

fun MovieDetailResponse.toMovieDetail() = MovieDetail(
	this.id,
	this.title,
	this.overview,
	this.vote_average,
	this.poster_path,
	this.backdrop_path,
	this.genres.toGenreList(),
)
