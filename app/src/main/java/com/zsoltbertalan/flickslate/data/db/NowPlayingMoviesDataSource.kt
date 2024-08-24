package com.zsoltbertalan.flickslate.data.db

import com.zsoltbertalan.flickslate.domain.model.Movie
import com.zsoltbertalan.flickslate.domain.model.PageData
import com.zsoltbertalan.flickslate.domain.model.PagingReply
import kotlinx.coroutines.flow.Flow

interface NowPlayingMoviesDataSource {

	suspend fun purgeDatabase()

	suspend fun insertNowPlayingMovies(movies: List<Movie>, page: Int)

	suspend fun insertNowPlayingMoviesPageData(page: PageData)

	fun getNowPlayingMovies(page: Int): Flow<PagingReply<Movie>?>

}
