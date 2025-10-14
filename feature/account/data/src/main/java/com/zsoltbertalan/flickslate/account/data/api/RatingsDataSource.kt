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

	}

}
