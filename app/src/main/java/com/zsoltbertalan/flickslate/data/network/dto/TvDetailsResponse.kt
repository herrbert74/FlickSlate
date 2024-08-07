package com.zsoltbertalan.flickslate.data.network.dto

import com.zsoltbertalan.flickslate.domain.model.TvDetail
import kotlinx.serialization.Serializable

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
data class TvDetailsResponse(
	val popularity: Float? = null,
	val vote_count: Int? = null,
	val poster_path: String? = null,
	val id: Int? = null,
	val adult: Boolean? = null,
	val backdrop_path: String? = null,
	val original_language: String? = null,
	val original_title: String? = null,
	val name: String? = null,
	val genres: List<Genres>? = null,
	val vote_average: Float? = null,
	val overview: String? = null,
	val first_air_date: String? = null,
	val number_of_episodes: Int? = null,
	val number_of_seasons: Int? = null,
	val status: String? = null,
)

fun TvDetailsResponse.toTvDetail() = TvDetail(
	this.id,
	this.name,
	this.overview,
	this.vote_average,
	this.poster_path,
	this.backdrop_path,
	this.genres.toGenreList(),
)
