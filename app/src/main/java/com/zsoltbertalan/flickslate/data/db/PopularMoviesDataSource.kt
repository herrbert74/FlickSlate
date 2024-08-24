package com.zsoltbertalan.flickslate.data.db

import com.zsoltbertalan.flickslate.domain.model.Movie
import com.zsoltbertalan.flickslate.domain.model.PageData
import com.zsoltbertalan.flickslate.domain.model.PagingReply
import kotlinx.coroutines.flow.Flow

interface PopularMoviesDataSource {

	suspend fun purgeDatabase()

	suspend fun insertPopularMovies(movies: List<Movie>, page: Int)

	suspend fun insertPopularMoviesPageData(page: PageData)

	fun getPopularMovies(page: Int): Flow<PagingReply<Movie>?>

}