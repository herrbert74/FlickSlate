package com.zsoltbertalan.flickslate.movies.data.network

import com.zsoltbertalan.flickslate.movies.data.api.UpcomingMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.network.model.UpcomingMoviesReplyDto
import com.zsoltbertalan.flickslate.movies.data.network.model.toMoviesReply
import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import com.zsoltbertalan.flickslate.shared.data.util.safeCallWithMetadata
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpcomingMoviesRemoteDataSource @Inject constructor(
	private val moviesService: MoviesService
) : UpcomingMoviesDataSource.Remote {

	override suspend fun getUpcomingMovies(etag: String?, page: Int?): Outcome<PagingReply<Movie>> {
		return com.zsoltbertalan.flickslate.shared.data.util.safeCallWithMetadata(
			{ moviesService.getUpcomingMovies(ifNoneMatch = etag, page = page) },
			Response<UpcomingMoviesReplyDto>::toMoviesReply
		)
	}

}
