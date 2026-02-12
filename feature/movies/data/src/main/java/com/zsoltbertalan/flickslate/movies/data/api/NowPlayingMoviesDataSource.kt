package com.zsoltbertalan.flickslate.movies.data.api

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import kotlinx.coroutines.flow.Flow

interface NowPlayingMoviesDataSource {

	interface Local {

		suspend fun purgeDatabase()

		suspend fun insertNowPlayingMovies(movies: List<Movie>, page: Int)

		suspend fun insertNowPlayingMoviesPageData(page: PageData)

		fun getNowPlayingMovies(page: Int): Flow<PagingReply<Movie>?>

		suspend fun getEtag(page: Int): String?

	}

	interface Remote {

		suspend fun getNowPlayingMovies(
			etag: String? = null,
			page: Int?,
			region: String? = null
		): Outcome<PagingReply<Movie>>

	}

}
