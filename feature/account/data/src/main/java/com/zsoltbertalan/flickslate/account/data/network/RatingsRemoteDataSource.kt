package com.zsoltbertalan.flickslate.account.data.network

import com.zsoltbertalan.flickslate.account.data.api.RatingsDataSource
import com.zsoltbertalan.flickslate.account.data.network.model.toRatedMovie
import com.zsoltbertalan.flickslate.account.data.network.model.toRatedTvEpisode
import com.zsoltbertalan.flickslate.account.data.network.model.toRatedTvShow
import com.zsoltbertalan.flickslate.account.domain.model.RatedMovie
import com.zsoltbertalan.flickslate.account.domain.model.RatedTvEpisode
import com.zsoltbertalan.flickslate.account.domain.model.RatedTvShow
import com.zsoltbertalan.flickslate.shared.data.util.safeCall
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import dagger.hilt.android.scopes.ActivityRetainedScoped
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ActivityRetainedScoped
internal class RatingsRemoteDataSource @Inject constructor(
	private val ratingsService: RatingsService,
) : RatingsDataSource.Remote {

	override suspend fun getRatedMovies(accountId: Int, sessionId: String): Outcome<List<RatedMovie>> {
		return safeCall(
			{ ratingsService.getRatedMovies(accountId, sessionId) },
			{ results.map { it.toRatedMovie() } }
		)
	}

	override suspend fun getRatedTvShows(accountId: Int, sessionId: String): Outcome<List<RatedTvShow>> {
		return safeCall(
			{ ratingsService.getRatedTvShows(accountId, sessionId) },
			{ results.map { it.toRatedTvShow() } }
		)
	}

	override suspend fun getRatedTvShowEpisodes(accountId: Int, sessionId: String): Outcome<List<RatedTvEpisode>> {
		return safeCall(
			{ ratingsService.getRatedTvShowEpisodes(accountId, sessionId) },
			{ results.map { it.toRatedTvEpisode() } }
		)
	}
}
