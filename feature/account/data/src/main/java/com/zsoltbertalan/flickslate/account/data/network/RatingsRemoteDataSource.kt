package com.zsoltbertalan.flickslate.account.data.network

import com.zsoltbertalan.flickslate.account.data.api.RatingsDataSource
import com.zsoltbertalan.flickslate.account.data.network.model.RatedMovieReplyDto
import com.zsoltbertalan.flickslate.account.data.network.model.RatedTvEpisodeReplyDto
import com.zsoltbertalan.flickslate.account.data.network.model.RatedTvShowReplyDto
import com.zsoltbertalan.flickslate.account.data.network.model.toRatedMoviesReply
import com.zsoltbertalan.flickslate.account.data.network.model.toRatedTvEpisodesReply
import com.zsoltbertalan.flickslate.account.data.network.model.toRatedTvShowsReply
import com.zsoltbertalan.flickslate.account.domain.model.RatedMovie
import com.zsoltbertalan.flickslate.account.domain.model.RatedTvEpisode
import com.zsoltbertalan.flickslate.account.domain.model.RatedTvShow
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.shared.data.util.safeCallWithMetadata
import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import retrofit2.Response

@ContributesBinding(ActivityRetainedScope::class)
@SingleIn(ActivityRetainedScope::class)
class RatingsRemoteDataSource @Inject internal constructor(
	private val ratingsService: RatingsService,
) : RatingsDataSource.Remote {

	override suspend fun getRatedMovies(
		accountId: Int,
		sessionId: String,
		page: Int
	): Outcome<PagingReply<RatedMovie>> {
		return safeCallWithMetadata(
			{ ratingsService.getRatedMovies(accountId, sessionId, page) },
			Response<RatedMovieReplyDto>::toRatedMoviesReply
		)
	}

	override suspend fun getRatedTvShows(
		accountId: Int,
		sessionId: String,
		page: Int
	): Outcome<PagingReply<RatedTvShow>> {
		return safeCallWithMetadata(
			{ ratingsService.getRatedTvShows(accountId, sessionId, page) },
			Response<RatedTvShowReplyDto>::toRatedTvShowsReply
		)
	}

	override suspend fun getRatedTvShowEpisodes(
		accountId: Int,
		sessionId: String,
		page: Int
	): Outcome<PagingReply<RatedTvEpisode>> {
		return safeCallWithMetadata(
			{ ratingsService.getRatedTvShowEpisodes(accountId, sessionId, page) },
			Response<RatedTvEpisodeReplyDto>::toRatedTvEpisodesReply
		)
	}
}
