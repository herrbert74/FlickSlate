package com.zsoltbertalan.flickslate.shared.data.network.model

import com.zsoltbertalan.flickslate.shared.domain.model.TvEpisodeDetail
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

@Serializable
@Suppress("PropertyName", "ConstructorParameterNaming")
data class TvEpisodeDetailsDto(
	val id: Int? = null,
	val show_id: Int? = null,
	val air_date: String? = null,
	val episode_number: Int? = null,
	val name: String? = null,
	val overview: String? = null,
	val season_number: Int? = null,
	val still_path: String? = null,
	val vote_average: Float? = null,
	val vote_count: Int? = null,
	val account_states: AccountStatesDto? = null,
)

@Serializable
data class AccountStatesDto(
	val rated: JsonElement? = null,
)

@Serializable
data class RatedDto(
	val value: Float? = null,
)

fun TvEpisodeDetailsDto.toTvEpisodeDetail(): TvEpisodeDetail {
	val ratedValue: Float? = this.account_states?.rated?.let {
		if (it is JsonObject) {
			Json.decodeFromJsonElement(RatedDto.serializer(), it).value
		} else {
			null
		}
	}
	return TvEpisodeDetail(
		this.id ?: 0,
		this.show_id ?: 0,
		this.air_date ?: "",
		this.episode_number ?: 0,
		this.name ?: "",
		this.overview ?: "",
		this.season_number ?: 0,
		this.still_path,
		this.vote_average ?: 0f,
		this.vote_count ?: 0,
		personalRating = ratedValue ?: -1f,
	)
}
