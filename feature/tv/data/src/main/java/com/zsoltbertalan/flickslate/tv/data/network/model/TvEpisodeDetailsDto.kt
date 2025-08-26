package com.zsoltbertalan.flickslate.tv.data.network.model

import com.zsoltbertalan.flickslate.tv.domain.model.TvEpisodeDetail
import kotlinx.serialization.Serializable

@Serializable
@Suppress("PropertyName", "ConstructorParameterNaming")
data class TvEpisodeDetailsDto(
	val id: Int? = null,
	val air_date: String? = null,
	val episode_number: Int? = null,
	val name: String? = null,
	val overview: String? = null,
	val season_number: Int? = null,
	val still_path: String? = null,
	val vote_average: Float? = null,
	val vote_count: Int? = null,
)

fun TvEpisodeDetailsDto.toTvEpisodeDetail() = TvEpisodeDetail(
	this.id ?: 0,
	this.air_date ?: "",
	this.episode_number ?: 0,
	this.name ?: "",
	this.overview ?: "",
	this.season_number ?: 0,
	this.still_path,
	this.vote_average ?: 0f,
	this.vote_count ?: 0
)
