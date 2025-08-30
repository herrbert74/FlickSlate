package com.zsoltbertalan.flickslate.movies.data.network

import com.zsoltbertalan.flickslate.movies.data.api.PopularMoviesDataSource
import com.zsoltbertalan.flickslate.shared.data.network.model.MoviesReplyDto
import com.zsoltbertalan.flickslate.shared.data.network.model.toMoviesReply
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
internal class PopularMoviesRemoteDataSource @Inject constructor(
	private val moviesService: MoviesService
) : PopularMoviesDataSource.Remote {

	override suspend fun getPopularMovies(etag: String?, page: Int?): Outcome<PagingReply<Movie>> {
		return safeCallWithMetadata(
			{ moviesService.getPopularMovies(ifNoneMatch = etag, page = page) },
			Response<MoviesReplyDto>::toMoviesReply
		)
	}

}
