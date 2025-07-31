package com.zsoltbertalan.flickslate.tv.data.network.model

import com.zsoltbertalan.flickslate.shared.data.network.model.GenreDto
import com.zsoltbertalan.flickslate.shared.data.network.model.toGenresReply
import com.zsoltbertalan.flickslate.tv.domain.model.TvDetail
import kotlinx.serialization.Serializable

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
data class TvDetailsDto(
	val popularity: Float? = null,
	val vote_count: Int? = null,
	val poster_path: String? = null,
	val id: Int? = null,
	val adult: Boolean? = null,
	val backdrop_path: String? = null,
	val original_language: String? = null,
	val original_title: String? = null,
	val name: String? = null,
	val genres: List<GenreDto>? = null,
	val vote_average: Float? = null,
	val overview: String? = null,
	val first_air_date: String? = null,
	val number_of_episodes: Int? = null,
	val number_of_seasons: Int? = null,
	val seasons: List<SeasonDto>? = null,
	val status: String? = null,
	val tagline: String? = null,
)

fun TvDetailsDto.toTvDetail() = TvDetail(
	this.id,
	this.name,
	this.overview,
	this.vote_average,
	this.poster_path,
	this.backdrop_path,
	this.genres.toGenresReply(),
	this.seasons.toSeasons(),
)
