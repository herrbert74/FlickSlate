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
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
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
				AccountListTestState.ratedMovieRatings
					.entries
					.sortedBy { it.key }
					.map { (movieId, rating) ->
						RatedMovie(
							movie = when (movieId) {
								0 -> RatedMovieMother.createRatedMovieList().first().movie
								else -> RatedMovieMother.createRatedMovieList().first().movie.copy(id = movieId, title = "Movie $movieId")
							},
							rating = rating
						)
					},
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
			AccountListTestState.ratedTvShowRatings
				.entries
				.sortedBy { it.key }
				.map { (tvShowId, rating) ->
					RatedTvMother.createRatedTvList().first().copy(
						tvShow = RatedTvMother.createRatedTvList().first().tvShow.copy(
							id = tvShowId,
							name = if (tvShowId == 0) "Detectorists" else "Tv $tvShowId",
						),
						rating = rating,
					)
				},
			isLastPage = true,
			PageData()
		)
	)

	override suspend fun getRatedTvShowEpisodes(accountId: Int, sessionId: String, page: Int):
		Outcome<PagingReply<RatedTvEpisode>> = Ok(
		PagingReply(
			if (AccountListTestState.includeRatedTvEpisode) RatedTvMother.createRatedEpisodeList() else emptyList(),
			isLastPage = true,
			PageData()
		)
	)

}
