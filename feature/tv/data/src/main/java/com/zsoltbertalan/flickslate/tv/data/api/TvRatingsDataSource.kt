package com.zsoltbertalan.flickslate.tv.data.api

import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome

internal interface TvRatingsDataSource {

	interface Remote {

		suspend fun rateTvShow(tvShowId: Int, rating: Float): Outcome<Unit>

		suspend fun deleteTvShowRating(tvShowId: Int): Outcome<Unit>

		suspend fun rateTvShowEpisode(tvShowId: Int, seasonNumber: Int, episodeNumber: Int, rating: Float): Outcome<Unit>

		suspend fun deleteTvShowEpisodeRating(tvShowId: Int, seasonNumber: Int, episodeNumber: Int): Outcome<Unit>

	}

}
