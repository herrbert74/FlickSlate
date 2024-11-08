package com.zsoltbertalan.flickslate.search.data.api

import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.search.domain.api.model.GenreMoviesPagingReply
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.util.Outcome
import kotlinx.coroutines.flow.Flow

interface GenreMoviesDataSource {

	interface Local {

		suspend fun purgeDatabase()

		suspend fun insertGenreMovies(genreId: Int, movies: List<Movie>, page: Int)

		suspend fun getEtag(genreId: Int, page: Int): String?

		suspend fun insertGenreMoviesPageData(genreId: Int, page: PageData)

		fun getGenreMovies(genreId: Int, page: Int): Flow<GenreMoviesPagingReply?>

	}

	interface Remote {

		suspend fun getGenreMovies(etag: String?, genreId: Int, page: Int?): Outcome<GenreMoviesPagingReply>

	}

}
