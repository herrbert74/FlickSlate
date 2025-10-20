package com.zsoltbertalan.flickslate

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.account.domain.api.RatingsRepository
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.MovieMother
import com.zsoltbertalan.flickslate.shared.domain.model.TvEpisodeDetail
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.tv.domain.model.TvMother
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class FakeRatingsRepository @Inject constructor() : RatingsRepository {

    override suspend fun getRatedMovies(accountId: Int, sessionId: String): Outcome<List<Movie>> = Ok(
        listOf(MovieMother.createDefaultMovie())
    )

    override suspend fun getRatedTvShows(accountId: Int, sessionId: String): Outcome<List<TvShow>> = Ok(
        listOf(TvMother.createDefaultTv())
    )

    override suspend fun getRatedTvShowEpisodes(accountId: Int, sessionId: String): Outcome<List<TvEpisodeDetail>> = Ok(
        listOf(TvMother.createSeasonDetail(1, 1).episodes.first())
    )

}
