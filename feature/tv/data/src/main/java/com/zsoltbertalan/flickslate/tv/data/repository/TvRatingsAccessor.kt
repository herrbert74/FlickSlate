package com.zsoltbertalan.flickslate.tv.data.repository

import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.tv.data.api.TvRatingsDataSource
import com.zsoltbertalan.flickslate.tv.domain.api.TvRatingsRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ActivityRetainedScoped
internal class TvRatingsAccessor @Inject constructor(
    private val tvRatingsRemoteDataSource: TvRatingsDataSource.Remote,
) : TvRatingsRepository {

    override suspend fun rateTvShow(tvShowId: Int, rating: Float, sessionId: String): Outcome<Unit> {
        return tvRatingsRemoteDataSource.rateTvShow(tvShowId, rating, sessionId)
    }

    override suspend fun deleteTvShowRating(tvShowId: Int, sessionId: String): Outcome<Unit> {
        return tvRatingsRemoteDataSource.deleteTvShowRating(tvShowId, sessionId)
    }

    override suspend fun rateTvShowEpisode(
		tvShowId: Int,
		seasonNumber: Int,
		episodeNumber: Int,
		rating: Float,
		sessionId: String
	): Outcome<Unit> {
        return tvRatingsRemoteDataSource.rateTvShowEpisode(tvShowId, seasonNumber, episodeNumber, rating, sessionId)
    }

    override suspend fun deleteTvShowEpisodeRating(
		tvShowId: Int,
		seasonNumber: Int,
		episodeNumber: Int,
		sessionId: String
	): Outcome<Unit> {
        return tvRatingsRemoteDataSource.deleteTvShowEpisodeRating(tvShowId, seasonNumber, episodeNumber, sessionId)
    }

}
