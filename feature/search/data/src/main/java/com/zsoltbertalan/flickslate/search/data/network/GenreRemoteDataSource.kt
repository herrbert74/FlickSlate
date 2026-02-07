package com.zsoltbertalan.flickslate.search.data.network

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.search.data.api.GenreDataSource
import com.zsoltbertalan.flickslate.search.data.network.model.GenreReplyDto
import com.zsoltbertalan.flickslate.search.data.network.model.toGenresReply
import com.zsoltbertalan.flickslate.shared.data.util.safeCallWithMetadata
import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import com.zsoltbertalan.flickslate.shared.domain.model.GenresReply
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import retrofit2.Response
import javax.inject.Inject

@ContributesBinding(ActivityRetainedScope::class)
@SingleIn(ActivityRetainedScope::class)
class GenreRemoteDataSource @Inject internal constructor(
	private val searchService: SearchService
) : GenreDataSource.Remote {

	override suspend fun getGenres(etag: String): Outcome<GenresReply> {
		return safeCallWithMetadata(
			{ searchService.getGenres(ifNoneMatch = etag) },
			Response<GenreReplyDto>::toGenresReply
		)
	}

}
