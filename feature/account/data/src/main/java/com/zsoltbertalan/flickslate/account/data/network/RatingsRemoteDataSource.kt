package com.zsoltbertalan.flickslate.account.data.network

import com.zsoltbertalan.flickslate.account.data.api.AccountDataSource
import com.zsoltbertalan.flickslate.account.data.api.RatingsDataSource
import com.zsoltbertalan.flickslate.shared.data.network.model.toMovieList
import com.zsoltbertalan.flickslate.shared.data.network.model.toTvEpisodeDetail
import com.zsoltbertalan.flickslate.shared.data.network.model.toTvList
import com.zsoltbertalan.flickslate.shared.data.util.runCatchingApi
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import com.zsoltbertalan.flickslate.shared.domain.model.TvEpisodeDetail
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import dagger.hilt.android.scopes.ActivityRetainedScoped
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ActivityRetainedScoped
internal class RatingsRemoteDataSource @Inject constructor(
	private val ratingsService: RatingsService,
	private val accountDataSource: AccountDataSource.Local,
) : RatingsDataSource.Remote {

	override suspend fun getRatedMovies(): Outcome<List<Movie>> {
		return runCatchingApi {
			val account = accountDataSource.getAccount() ?: throw Exception("User not logged in")
			val sessionId = accountDataSource.getAccessToken() ?: throw Exception("User not logged in")
			ratingsService.getRatedMovies(account.id, sessionId).results.toMovieList()
		}
	}

	override suspend fun getRatedTvShows(): Outcome<List<TvShow>> {
		return runCatchingApi {
			val account = accountDataSource.getAccount() ?: throw Exception("User not logged in")
			val sessionId = accountDataSource.getAccessToken() ?: throw Exception("User not logged in")
			ratingsService.getRatedTvShows(account.id, sessionId).results.toTvList()
		}
	}

	override suspend fun getRatedTvShowEpisodes(): Outcome<List<TvEpisodeDetail>> {
		return runCatchingApi {
			val account = accountDataSource.getAccount() ?: throw Exception("User not logged in")
			val sessionId = accountDataSource.getAccessToken() ?: throw Exception("User not logged in")
			ratingsService.getRatedTvShowEpisodes(account.id, sessionId).map { it.toTvEpisodeDetail() }
		}
	}
}
