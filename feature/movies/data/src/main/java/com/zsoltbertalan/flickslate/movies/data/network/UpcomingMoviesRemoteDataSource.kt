package com.zsoltbertalan.flickslate.movies.data.network

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.movies.data.api.UpcomingMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.network.model.UpcomingMoviesReplyDto
import com.zsoltbertalan.flickslate.movies.data.network.model.toMoviesReply
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
@Inject
class UpcomingMoviesRemoteDataSource internal constructor(
	private val moviesService: MoviesService
) : UpcomingMoviesDataSource.Remote {

	override suspend fun getUpcomingMovies(etag: String?, page: Int?, region: String?): Outcome<PagingReply<Movie>> {
		return safeCallWithMetadata(
			{ moviesService.getUpcomingMovies(ifNoneMatch = etag, page = page, region = region) },
			Response<UpcomingMoviesReplyDto>::toMoviesReply
		)
	}

}
