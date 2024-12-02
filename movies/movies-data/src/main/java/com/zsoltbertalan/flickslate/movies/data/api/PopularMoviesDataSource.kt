package com.zsoltbertalan.flickslate.movies.data.api

import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.model.PageData
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import kotlinx.coroutines.flow.Flow

interface PopularMoviesDataSource {

	interface Local {

		suspend fun purgeDatabase()

		suspend fun insertPopularMovies(movies: List<Movie>, page: Int)

		suspend fun insertPopularMoviesPageData(page: PageData)

		fun getPopularMovies(page: Int): Flow<PagingReply<Movie>?>

		suspend fun getEtag(page: Int): String?

	}

	interface Remote {

		suspend fun getPopularMovies(etag: String? = null, page: Int? = null): Outcome<PagingReply<Movie>>

	}

}
