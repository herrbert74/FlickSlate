package com.zsoltbertalan.flickslate.tv.data.network.model

import com.zsoltbertalan.flickslate.shared.data.util.mapImmutableNullInputList
import com.zsoltbertalan.flickslate.tv.domain.model.Season
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.Serializable

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
data class SeasonDto(
	val air_date: String?,
	val episode_count: Int?,
	val id: Int,
	val name: String?,
	val overview: String?,
	val poster_path: String?,
	val season_number: Int?
)

internal fun SeasonDto.toSeason() = Season(
	this.air_date ?: "",
	this.episode_count ?: 0,
	this.id,
	this.name,
	this.overview ?: "",
	this.poster_path,
	this.season_number ?: 0,
)

internal fun List<SeasonDto>?.toSeasons(): ImmutableList<Season> =
	mapImmutableNullInputList(this) { season -> season.toSeason() }
