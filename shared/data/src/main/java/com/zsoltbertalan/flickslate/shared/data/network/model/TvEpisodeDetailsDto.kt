package com.zsoltbertalan.flickslate.shared.data.network.model

import com.babestudios.base.data.mapNullInputList
import com.zsoltbertalan.flickslate.shared.domain.model.TvEpisodeDetail
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
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

fun List<TvEpisodeDetailsDto>.toTvEpisodeList(): List<TvEpisodeDetail> =
	mapNullInputList(this) { tvDto -> tvDto.toTvEpisodeDetail() }

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
