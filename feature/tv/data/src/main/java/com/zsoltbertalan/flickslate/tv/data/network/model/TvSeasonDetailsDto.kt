package com.zsoltbertalan.flickslate.tv.data.network.model

import com.zsoltbertalan.flickslate.shared.data.network.model.TvEpisodeDetailsDto
import com.zsoltbertalan.flickslate.shared.data.network.model.toTvEpisodeDetail
import com.zsoltbertalan.flickslate.tv.domain.model.SeasonDetail
import kotlinx.serialization.Serializable

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
data class TvSeasonDetailsDto(
	val _id: String? = null,
	val air_date: String? = null,
	val episodes: List<TvEpisodeDetailsDto>? = null,
	val name: String? = null,
	val overview: String? = null,
	val id: Int? = null,
	val poster_path: String? = null,
	val season_number: Int? = null,
	val vote_average: Float? = null
)

internal fun TvSeasonDetailsDto.toTvSeasonDetails() = SeasonDetail(
	this.air_date ?: "",
	this.episodes?.size ?: 0,
	this.id ?: 0,
	this.name ?: "",
	this.overview,
	this.poster_path,
	this.season_number ?: 0,
	this.episodes?.map { it.toTvEpisodeDetail() } ?: emptyList(),
	this.vote_average ?: 0f
)
