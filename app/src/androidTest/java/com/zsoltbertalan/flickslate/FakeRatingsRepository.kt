package com.zsoltbertalan.flickslate

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.account.domain.api.RatingsRepository
import com.zsoltbertalan.flickslate.account.domain.model.RatedMovie
import com.zsoltbertalan.flickslate.account.domain.model.RatedMovieMother
import com.zsoltbertalan.flickslate.account.domain.model.RatedTvEpisode
import com.zsoltbertalan.flickslate.account.domain.model.RatedTvMother
import com.zsoltbertalan.flickslate.account.domain.model.RatedTvShow
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class FakeRatingsRepository @Inject constructor() : RatingsRepository {

	override suspend fun getRatedMovies(
		accountId: Int,
		sessionId: String,
		page: Int
	): Outcome<PagingReply<RatedMovie>> =
		Ok(
			PagingReply(
				RatedMovieMother.createRatedMovieList(),
				isLastPage = true,
				PageData()
			)
		)

	override suspend fun getRatedTvShows(
		accountId: Int,
		sessionId: String,
		page: Int
	): Outcome<PagingReply<RatedTvShow>> = Ok(
		PagingReply(
			RatedTvMother.createRatedTvList(),
			isLastPage = true,
			PageData()
		)
	)

	override suspend fun getRatedTvShowEpisodes(accountId: Int, sessionId: String, page: Int):
		Outcome<PagingReply<RatedTvEpisode>> = Ok(
		PagingReply(
			RatedTvMother.createRatedEpisodeList(),
			isLastPage = true,
			PageData()
		)
	)

}
