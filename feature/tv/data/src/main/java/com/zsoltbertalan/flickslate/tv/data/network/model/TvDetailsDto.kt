package com.zsoltbertalan.flickslate.tv.data.network.model

import com.zsoltbertalan.flickslate.shared.data.network.model.GenreDto
import com.zsoltbertalan.flickslate.shared.data.network.model.toGenresReply
import com.zsoltbertalan.flickslate.tv.domain.model.TvDetail
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

private const val YEAR_CHARS = 4

@Serializable
@Suppress("PropertyName", "ConstructorParameterNaming")
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
	val last_air_date: String? = null,
	val number_of_episodes: Int? = null,
	val number_of_seasons: Int? = null,
	val seasons: List<SeasonDto>? = null,
	val status: String? = null,
	val tagline: String? = null,
	val account_states: AccountStatesDto? = null,
)

@Serializable
data class AccountStatesDto(
	val rated: JsonElement? = null,
	val favorite: Boolean = false,
)

@Serializable
data class RatedDto(
	val value: Float? = null,
)

internal fun TvDetailsDto.toTvDetail(): TvDetail {
	val ratedValue: Float? = this.account_states?.rated?.let {
		if (it is JsonObject) {
			Json.decodeFromJsonElement(RatedDto.serializer(), it).value
		} else {
			null
		}
	}

	return TvDetail(
		id = this.id,
		title = this.name,
		overview = this.overview,
		voteAverage = this.vote_average,
		posterPath = this.poster_path,
		backdropPath = this.backdrop_path,
		genres = this.genres.toGenresReply(),
		seasons = this.seasons.toSeasons(),
		status = this.status,
		tagline = this.tagline,
		years = "${this.first_air_date?.substring(0, YEAR_CHARS)} - ${this.last_air_date?.substring(0, YEAR_CHARS)}",
		personalRating = ratedValue ?: -1f,
		favorite = this.account_states?.favorite ?: false,
	)
}
