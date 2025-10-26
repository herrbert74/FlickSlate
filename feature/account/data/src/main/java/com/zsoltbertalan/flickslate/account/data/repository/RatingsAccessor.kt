package com.zsoltbertalan.flickslate.account.data.repository

import com.zsoltbertalan.flickslate.account.data.api.RatingsDataSource
import com.zsoltbertalan.flickslate.account.domain.api.RatingsRepository
import com.zsoltbertalan.flickslate.account.domain.model.RatedMovie
import com.zsoltbertalan.flickslate.shared.domain.model.TvEpisodeDetail
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
internal class RatingsAccessor @Inject constructor(
    private val ratingsRemoteDataSource: RatingsDataSource.Remote,
) : RatingsRepository {

    override suspend fun getRatedMovies(accountId: Int, sessionId: String): Outcome<List<RatedMovie>> {
        return ratingsRemoteDataSource.getRatedMovies(accountId, sessionId)
    }

    override suspend fun getRatedTvShows(accountId: Int, sessionId: String): Outcome<List<TvShow>> {
        return ratingsRemoteDataSource.getRatedTvShows(accountId, sessionId)
    }

    override suspend fun getRatedTvShowEpisodes(accountId: Int, sessionId: String): Outcome<List<TvEpisodeDetail>> {
        return ratingsRemoteDataSource.getRatedTvShowEpisodes(accountId, sessionId)
    }

}
