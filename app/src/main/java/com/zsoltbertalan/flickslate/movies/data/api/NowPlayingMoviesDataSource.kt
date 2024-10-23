package com.zsoltbertalan.flickslate.movies.data.api

import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import kotlinx.coroutines.flow.Flow

interface NowPlayingMoviesDataSource {

	interface Local {
		suspend fun purgeDatabase()

		suspend fun insertNowPlayingMovies(movies: List<Movie>, page: Int)

		suspend fun insertNowPlayingMoviesPageData(page: PageData)

		fun getNowPlayingMovies(page: Int): Flow<PagingReply<Movie>?>
	}

	interface Remote {

		suspend fun getNowPlayingMovies(page: Int?): Outcome<PagingReply<Movie>>

	}

}
