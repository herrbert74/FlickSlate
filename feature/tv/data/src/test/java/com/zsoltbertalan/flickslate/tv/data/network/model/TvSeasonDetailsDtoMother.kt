package com.zsoltbertalan.flickslate.tv.data.network.model

import com.zsoltbertalan.flickslate.shared.data.network.model.TvEpisodeDetailsDto

internal object TvSeasonDetailsDtoMother {

	fun createSeasonDetailsDto(): TvSeasonDetailsDto {
		return TvSeasonDetailsDto(
			_id = "season-id",
			air_date = "2020-01-01",
			episodes = listOf(
				TvEpisodeDetailsDto(
					id = 10,
					show_id = 5,
					air_date = "2020-01-01",
					episode_number = 1,
					name = "Pilot",
					overview = "Intro",
					season_number = 1,
					still_path = "/still.jpg",
					vote_average = 7.5f,
					vote_count = 100,
				)
			),
			name = "Season 1",
			overview = "Season overview",
			id = 2,
			poster_path = "/season.jpg",
			season_number = 1,
			vote_average = 7.0f,
		)
	}
}
