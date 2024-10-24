package com.zsoltbertalan.flickslate.search.data.api

import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import kotlinx.coroutines.flow.Flow

interface GenreMoviesDataSource {

	interface Local {

		suspend fun purgeDatabase()

		suspend fun insertGenreMovies(movies: List<Movie>, page: Int)

		suspend fun insertGenreMoviesPageData(page: PageData)

		fun getGenreMovies(page: Int): Flow<PagingReply<Movie>?>

	}

	interface Remote {

		suspend fun getGenreMovies(genreId: Int, page: Int?): Outcome<PagingReply<Movie>>

	}

}
