package com.zsoltbertalan.flickslate.search.data.network

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.search.data.api.GenreDataSource
import com.zsoltbertalan.flickslate.search.data.network.model.GenreReplyDto
import com.zsoltbertalan.flickslate.search.data.network.model.toGenresReply
import com.zsoltbertalan.flickslate.shared.data.util.safeCallWithMetadata
import com.zsoltbertalan.flickslate.shared.domain.model.GenresReply
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import retrofit2.Response

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
@Inject
class GenreRemoteDataSource internal constructor(
	private val searchService: SearchService
) : GenreDataSource.Remote {

	override suspend fun getGenres(etag: String): Outcome<GenresReply> {
		return safeCallWithMetadata(
			{ searchService.getGenres(ifNoneMatch = etag) },
			Response<GenreReplyDto>::toGenresReply
		)
	}

}
