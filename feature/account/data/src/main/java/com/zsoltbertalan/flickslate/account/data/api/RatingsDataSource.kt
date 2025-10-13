package com.zsoltbertalan.flickslate.account.data.api

import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import com.zsoltbertalan.flickslate.shared.domain.model.TvEpisodeDetail
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome

internal interface RatingsDataSource {

	interface Remote {

		suspend fun getRatedMovies(): Outcome<List<Movie>>

		suspend fun getRatedTvShows(): Outcome<List<TvShow>>

		suspend fun getRatedTvShowEpisodes(): Outcome<List<TvEpisodeDetail>>

		suspend fun rateMovie(movieId: Int, rating: Float): Outcome<Unit>

		suspend fun deleteMovieRating(movieId: Int): Outcome<Unit>

		suspend fun rateTvShow(tvShowId: Int, rating: Float): Outcome<Unit>

		suspend fun deleteTvShowRating(tvShowId: Int): Outcome<Unit>

		suspend fun rateTvShowEpisode(tvShowId: Int, seasonNumber: Int, episodeNumber: Int, rating: Float): Outcome<Unit>

		suspend fun deleteTvShowEpisodeRating(tvShowId: Int, seasonNumber: Int, episodeNumber: Int): Outcome<Unit>

	}

}
