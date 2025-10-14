package com.zsoltbertalan.flickslate.account.domain.api

import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import com.zsoltbertalan.flickslate.shared.domain.model.TvEpisodeDetail
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome

interface RatingsRepository {

	suspend fun getRatedMovies(accountId: Int, sessionId: String): Outcome<List<Movie>>

	suspend fun getRatedTvShows(accountId: Int, sessionId: String): Outcome<List<TvShow>>

	suspend fun getRatedTvShowEpisodes(accountId: Int, sessionId: String): Outcome<List<TvEpisodeDetail>>

}
