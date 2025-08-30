package com.zsoltbertalan.flickslate.movies.data.network

import com.zsoltbertalan.flickslate.movies.data.api.UpcomingMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.network.model.UpcomingMoviesReplyDto
import com.zsoltbertalan.flickslate.movies.data.network.model.toMoviesReply
import com.zsoltbertalan.flickslate.shared.data.util.safeCallWithMetadata
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Response
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ActivityRetainedScoped
internal class UpcomingMoviesRemoteDataSource @Inject constructor(
	private val moviesService: MoviesService
) : UpcomingMoviesDataSource.Remote {

	override suspend fun getUpcomingMovies(etag: String?, page: Int?): Outcome<PagingReply<Movie>> {
		return safeCallWithMetadata(
			{ moviesService.getUpcomingMovies(ifNoneMatch = etag, page = page) },
			Response<UpcomingMoviesReplyDto>::toMoviesReply
		)
	}

}
