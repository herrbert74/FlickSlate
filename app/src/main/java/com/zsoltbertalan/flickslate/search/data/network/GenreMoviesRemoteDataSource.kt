package com.zsoltbertalan.flickslate.search.data.network

import com.zsoltbertalan.flickslate.movies.data.network.model.MoviesReplyDto
import com.zsoltbertalan.flickslate.movies.data.network.model.toMoviesReply
import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.search.data.api.GenreMoviesDataSource
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import com.zsoltbertalan.flickslate.shared.util.safeCallWithMetadata
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreMoviesRemoteDataSource @Inject constructor(
	private val searchService: SearchService
) : GenreMoviesDataSource.Remote {

	override suspend fun getGenreMovies(genreId: Int, page: Int?): Outcome<PagingReply<Movie>> {
		return safeCallWithMetadata(
			{ searchService.getGenreMovie(withGenres = genreId, page = page) },
			Response<MoviesReplyDto>::toMoviesReply
		)
	}

}

