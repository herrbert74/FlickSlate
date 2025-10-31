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
import com.zsoltbertalan.flickslate.shared.data.util.safeCallWithMetadata
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Response
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ActivityRetainedScoped
internal class RatingsRemoteDataSource @Inject constructor(
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
