package com.zsoltbertalan.flickslate.data.db

import com.zsoltbertalan.flickslate.domain.model.Movie
import com.zsoltbertalan.flickslate.domain.model.PageData
import com.zsoltbertalan.flickslate.domain.model.PagingReply
import kotlinx.coroutines.flow.Flow

interface UpcomingMoviesDataSource {

	suspend fun purgeDatabase()

	suspend fun insertUpcomingMovies(movies: List<Movie>, page: Int)

	suspend fun insertUpcomingMoviesPageData(page: PageData)

	fun getUpcomingMovies(page: Int): Flow<PagingReply<Movie>?>

}