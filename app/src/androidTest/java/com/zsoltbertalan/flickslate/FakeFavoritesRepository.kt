package com.zsoltbertalan.flickslate

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.account.domain.api.FavoritesRepository
import com.zsoltbertalan.flickslate.account.domain.model.FavoriteMovie
import com.zsoltbertalan.flickslate.account.domain.model.FavoriteTvShow
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class FakeFavoritesRepository @Inject constructor() : FavoritesRepository {

	override suspend fun getFavoriteMovies(
		accountId: Int,
		sessionId: String,
		page: Int
	): Outcome<PagingReply<FavoriteMovie>> =
		Ok(
			PagingReply(
				pagingList = listOf(
					FavoriteMovie(
						movie = Movie(
							id = 1,
							title = "Brazil",
						)
					)
				),
				isLastPage = true,
				pageData = PageData(page = page, totalPages = 1, totalResults = 1),
			)
		)

	override suspend fun getFavoriteTvShows(
		accountId: Int,
		sessionId: String,
		page: Int
	): Outcome<PagingReply<FavoriteTvShow>> =
		Ok(
			PagingReply(
				pagingList = listOf(
					FavoriteTvShow(
						tvShow = TvShow(
							id = 2,
							name = "Detectorists",
						)
					)
				),
				isLastPage = true,
				pageData = PageData(page = page, totalPages = 1, totalResults = 1),
			)
		)
}
