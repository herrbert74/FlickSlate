package com.zsoltbertalan.flickslate.account.domain.api

import com.zsoltbertalan.flickslate.account.domain.model.RatedMovie
import com.zsoltbertalan.flickslate.account.domain.model.RatedTvEpisode
import com.zsoltbertalan.flickslate.account.domain.model.RatedTvShow
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome

interface RatingsRepository {

	suspend fun getRatedMovies(accountId: Int, sessionId: String, page: Int): Outcome<PagingReply<RatedMovie>>

	suspend fun getRatedTvShows(accountId: Int, sessionId: String, page: Int): Outcome<PagingReply<RatedTvShow>>

	suspend fun getRatedTvShowEpisodes(accountId: Int, sessionId: String, page: Int): Outcome<PagingReply<RatedTvEpisode>>

}
