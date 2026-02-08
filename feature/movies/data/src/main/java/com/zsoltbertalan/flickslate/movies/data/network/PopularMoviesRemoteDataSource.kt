package com.zsoltbertalan.flickslate.movies.data.network

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.movies.data.api.PopularMoviesDataSource
import com.zsoltbertalan.flickslate.shared.data.network.model.MoviesReplyDto
import com.zsoltbertalan.flickslate.shared.data.network.model.toMoviesReply
import com.zsoltbertalan.flickslate.shared.data.util.safeCallWithMetadata
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import retrofit2.Response

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class PopularMoviesRemoteDataSource @Inject internal constructor(
	private val moviesService: MoviesService
) : PopularMoviesDataSource.Remote {

	override suspend fun getPopularMovies(etag: String?, page: Int?): Outcome<PagingReply<Movie>> {
		return safeCallWithMetadata(
			{ moviesService.getPopularMovies(ifNoneMatch = etag, page = page) },
			Response<MoviesReplyDto>::toMoviesReply
		)
	}

}
