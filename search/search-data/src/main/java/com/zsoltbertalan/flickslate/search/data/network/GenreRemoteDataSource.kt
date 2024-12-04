package com.zsoltbertalan.flickslate.search.data.network

import com.zsoltbertalan.flickslate.search.data.api.GenreDataSource
import com.zsoltbertalan.flickslate.search.data.network.model.GenreReplyDto
import com.zsoltbertalan.flickslate.search.data.network.model.toGenresReply
import com.zsoltbertalan.flickslate.shared.data.util.safeCallWithMetadata
import com.zsoltbertalan.flickslate.shared.model.GenresReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreRemoteDataSource @Inject constructor(
	private val searchService: SearchService
) : GenreDataSource.Remote {

	override suspend fun getGenres(etag: String): Outcome<GenresReply> {
		return safeCallWithMetadata(
			{ searchService.getGenres(ifNoneMatch = etag) },
			Response<GenreReplyDto>::toGenresReply
		)
	}

}

