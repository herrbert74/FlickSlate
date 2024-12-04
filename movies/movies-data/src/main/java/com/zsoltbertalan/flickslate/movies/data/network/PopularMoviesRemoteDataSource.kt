package com.zsoltbertalan.flickslate.movies.data.network

import com.zsoltbertalan.flickslate.movies.data.api.PopularMoviesDataSource
import com.zsoltbertalan.flickslate.shared.data.network.model.MoviesReplyDto
import com.zsoltbertalan.flickslate.shared.data.network.model.toMoviesReply
import com.zsoltbertalan.flickslate.shared.model.Movie
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import com.zsoltbertalan.flickslate.shared.data.util.safeCallWithMetadata
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PopularMoviesRemoteDataSource @Inject constructor(
	private val moviesService: MoviesService
) : PopularMoviesDataSource.Remote {

	override suspend fun getPopularMovies(etag: String?, page: Int?): Outcome<PagingReply<Movie>> {
		return safeCallWithMetadata(
			{ moviesService.getPopularMovies(ifNoneMatch = etag, page = page) },
			Response<MoviesReplyDto>::toMoviesReply
		)
	}

}