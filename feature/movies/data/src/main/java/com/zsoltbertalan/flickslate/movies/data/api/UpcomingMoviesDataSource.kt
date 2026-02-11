package com.zsoltbertalan.flickslate.movies.data.api

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import kotlinx.coroutines.flow.Flow

interface UpcomingMoviesDataSource {

	interface Local {

		suspend fun purgeDatabase()

		suspend fun insertUpcomingMovies(movies: List<Movie>, page: Int)

		suspend fun insertUpcomingMoviesPageData(page: PageData)

		fun getUpcomingMovies(page: Int): Flow<PagingReply<Movie>?>

		suspend fun getEtag(page: Int): String?

	}

	interface Remote {

		suspend fun getUpcomingMovies(
			etag: String? = null,
			page: Int?,
			region: String? = null
		): Outcome<PagingReply<Movie>>

	}

}
