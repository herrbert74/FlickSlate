package com.zsoltbertalan.flickslate.tv.data.network.model

import com.zsoltbertalan.flickslate.shared.data.network.model.GenreDto
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

internal object TvDetailsDtoMother {

	fun createTvDetailsDto(accountStatesRating: Float? = null): TvDetailsDto {
		return TvDetailsDto(
			popularity = 1f,
			vote_count = 10,
			poster_path = "/poster.jpg",
			id = 1,
			adult = false,
			backdrop_path = "/backdrop.jpg",
			original_language = "en",
			original_title = "Original",
			name = "Detectorists",
			genres = listOf(GenreDto(id = 1, name = "Comedy")),
			vote_average = 8.5f,
			overview = "Overview",
			first_air_date = "2020-01-01",
			last_air_date = "2020-12-31",
			number_of_episodes = 10,
			number_of_seasons = 1,
			seasons = listOf(
				SeasonDto(
					air_date = "2020-01-01",
					episode_count = 10,
					id = 2,
					name = "Season 1",
					overview = "Season overview",
					poster_path = "/season.jpg",
					season_number = 1,
				)
			),
			status = "Ended",
			tagline = "Tagline",
			account_states = accountStatesRating?.let { rating ->
				val ratedJson: JsonElement = Json.encodeToJsonElement(
					RatedDto.serializer(),
					RatedDto(value = rating)
				)
				AccountStatesDto(rated = ratedJson)
			}
		)
	}
}

