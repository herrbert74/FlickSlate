package com.zsoltbertalan.flickslate.account.data.api

import com.zsoltbertalan.flickslate.account.domain.model.RatedMovie
import com.zsoltbertalan.flickslate.account.domain.model.RatedTvEpisode
import com.zsoltbertalan.flickslate.account.domain.model.RatedTvShow
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply

interface RatingsDataSource {

	interface Remote {

		suspend fun getRatedMovies(accountId: Int, sessionId: String, page: Int): Outcome<PagingReply<RatedMovie>>

		suspend fun getRatedTvShows(accountId: Int, sessionId: String, page: Int): Outcome<PagingReply<RatedTvShow>>

		suspend fun getRatedTvShowEpisodes(
			accountId: Int,
			sessionId: String,
			page: Int
		): Outcome<PagingReply<RatedTvEpisode>>

	}

}
