package com.zsoltbertalan.flickslate.account.data.network

import com.zsoltbertalan.flickslate.account.data.api.RatingsDataSource
import com.zsoltbertalan.flickslate.shared.data.network.model.toMovieList
import com.zsoltbertalan.flickslate.shared.data.network.model.toTvEpisodeList
import com.zsoltbertalan.flickslate.shared.data.network.model.toTvList
import com.zsoltbertalan.flickslate.shared.data.util.safeCall
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.TvEpisodeDetail
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import dagger.hilt.android.scopes.ActivityRetainedScoped
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ActivityRetainedScoped
internal class RatingsRemoteDataSource @Inject constructor(
	private val ratingsService: RatingsService,
) : RatingsDataSource.Remote {

	override suspend fun getRatedMovies(accountId: Int, sessionId: String): Outcome<List<Movie>> {
		return safeCall(
			{ ratingsService.getRatedMovies(accountId, sessionId) },
			{ results.toMovieList() }
		)
	}

	override suspend fun getRatedTvShows(accountId: Int, sessionId: String): Outcome<List<TvShow>> {
		return safeCall(
			{ ratingsService.getRatedTvShows(accountId, sessionId) },
			{ results.toTvList() }
		)
	}

	override suspend fun getRatedTvShowEpisodes(accountId: Int, sessionId: String): Outcome<List<TvEpisodeDetail>> {
		return safeCall(
			{ ratingsService.getRatedTvShowEpisodes(accountId, sessionId) },
			{ results.toTvEpisodeList() }
		)
	}
}
