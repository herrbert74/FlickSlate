package com.zsoltbertalan.flickslate.search.data.network

import com.github.michaelbull.result.map
import com.zsoltbertalan.flickslate.search.data.api.GenreMoviesDataSource
import com.zsoltbertalan.flickslate.search.domain.api.model.GenreMoviesPagingReply
import com.zsoltbertalan.flickslate.shared.data.network.model.MoviesReplyDto
import com.zsoltbertalan.flickslate.shared.data.network.model.toMoviesReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreMoviesRemoteDataSource @Inject constructor(
	private val searchService: SearchService
) : GenreMoviesDataSource.Remote {

	override suspend fun getGenreMovies(etag: String?, genreId: Int, page: Int?): Outcome<GenreMoviesPagingReply> {
		return com.zsoltbertalan.flickslate.shared.data.util.safeCallWithMetadata(
			{ searchService.getGenreMovie(ifNoneMatch = etag, withGenres = genreId, page = page) },
			Response<MoviesReplyDto>::toMoviesReply
		).map {
			GenreMoviesPagingReply(genreId, it)
		}
	}

}

