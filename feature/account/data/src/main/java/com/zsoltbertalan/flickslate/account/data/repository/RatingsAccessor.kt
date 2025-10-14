package com.zsoltbertalan.flickslate.account.data.repository

import com.zsoltbertalan.flickslate.account.data.api.RatingsDataSource
import com.zsoltbertalan.flickslate.account.domain.api.RatingsRepository
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import com.zsoltbertalan.flickslate.shared.domain.model.TvEpisodeDetail
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import dagger.hilt.android.scopes.ActivityRetainedScoped
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ActivityRetainedScoped
internal class RatingsAccessor @Inject constructor(
    private val ratingsRemoteDataSource: RatingsDataSource.Remote,
) : RatingsRepository {

    override suspend fun getRatedMovies(): Outcome<List<Movie>> {
        return ratingsRemoteDataSource.getRatedMovies()
    }

    override suspend fun getRatedTvShows(): Outcome<List<TvShow>> {
        return ratingsRemoteDataSource.getRatedTvShows()
    }

    override suspend fun getRatedTvShowEpisodes(): Outcome<List<TvEpisodeDetail>> {
        return ratingsRemoteDataSource.getRatedTvShowEpisodes()
    }

}
