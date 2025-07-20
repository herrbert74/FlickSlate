package com.zsoltbertalan.flickslate.search.data.network

import com.zsoltbertalan.flickslate.search.data.api.GenreDataSource
import com.zsoltbertalan.flickslate.search.data.network.model.GenreReplyDto
import com.zsoltbertalan.flickslate.search.data.network.model.toGenresReply
import com.zsoltbertalan.flickslate.shared.data.util.safeCallWithMetadata
import com.zsoltbertalan.flickslate.shared.model.GenresReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Response
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ViewModelScoped
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
