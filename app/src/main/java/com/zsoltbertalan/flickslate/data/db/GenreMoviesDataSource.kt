package com.zsoltbertalan.flickslate.data.db

import com.zsoltbertalan.flickslate.domain.model.Movie
import com.zsoltbertalan.flickslate.domain.model.PageData
import com.zsoltbertalan.flickslate.domain.model.PagingReply
import kotlinx.coroutines.flow.Flow

interface GenreMoviesDataSource {

	suspend fun purgeDatabase()

	suspend fun insertGenreMovies(movies: List<Movie>, page: Int)

	suspend fun insertGenreMoviesPageData(page: PageData)

	fun getGenreMovies(page: Int): Flow<PagingReply<Movie>?>

}