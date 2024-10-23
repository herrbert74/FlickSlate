package com.zsoltbertalan.flickslate.movies.data.api

import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import kotlinx.coroutines.flow.Flow

interface UpcomingMoviesDataSource {

	interface Local {

		suspend fun purgeDatabase()

		suspend fun insertUpcomingMovies(movies: List<Movie>, page: Int)

		suspend fun insertUpcomingMoviesPageData(page: PageData)

		fun getUpcomingMovies(page: Int): Flow<PagingReply<Movie>?>

	}

	interface Remote {

		suspend fun getUpcomingMovies(page: Int?): Outcome<PagingReply<Movie>>

	}

}
