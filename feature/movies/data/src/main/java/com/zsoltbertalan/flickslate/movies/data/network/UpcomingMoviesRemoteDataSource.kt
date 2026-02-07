package com.zsoltbertalan.flickslate.movies.data.network

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.movies.data.api.UpcomingMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.network.model.UpcomingMoviesReplyDto
import com.zsoltbertalan.flickslate.movies.data.network.model.toMoviesReply
import com.zsoltbertalan.flickslate.shared.data.util.safeCallWithMetadata
import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import retrofit2.Response
import javax.inject.Inject

@ContributesBinding(ActivityRetainedScope::class)
@SingleIn(ActivityRetainedScope::class)
internal class UpcomingMoviesRemoteDataSource @Inject constructor(
	private val moviesService: MoviesService
) : UpcomingMoviesDataSource.Remote {

	override suspend fun getUpcomingMovies(etag: String?, page: Int?, region: String?): Outcome<PagingReply<Movie>> {
		return safeCallWithMetadata(
			{ moviesService.getUpcomingMovies(ifNoneMatch = etag, page = page, region = region) },
			Response<UpcomingMoviesReplyDto>::toMoviesReply
		)
	}

}
