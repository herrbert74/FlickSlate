package com.zsoltbertalan.flickslate.account.data.repository

import com.zsoltbertalan.flickslate.account.data.api.RatingsDataSource
import com.zsoltbertalan.flickslate.account.domain.api.RatingsRepository
import com.zsoltbertalan.flickslate.account.domain.model.RatedMovie
import com.zsoltbertalan.flickslate.account.domain.model.RatedTvEpisode
import com.zsoltbertalan.flickslate.account.domain.model.RatedTvShow
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
@Inject
class RatingsAccessor internal constructor(
	private val ratingsRemoteDataSource: RatingsDataSource.Remote,
) : RatingsRepository {

	override suspend fun getRatedMovies(
		accountId: Int,
		sessionId: String,
		page: Int
	): Outcome<PagingReply<RatedMovie>> {
		return ratingsRemoteDataSource.getRatedMovies(accountId, sessionId, page)
	}

	override suspend fun getRatedTvShows(
		accountId: Int,
		sessionId: String,
		page: Int
	): Outcome<PagingReply<RatedTvShow>> {
		return ratingsRemoteDataSource.getRatedTvShows(accountId, sessionId, page)
	}

	override suspend fun getRatedTvShowEpisodes(
		accountId: Int,
		sessionId: String,
		page: Int
	): Outcome<PagingReply<RatedTvEpisode>> {
		return ratingsRemoteDataSource.getRatedTvShowEpisodes(accountId, sessionId, page)
	}

}
