package com.zsoltbertalan.flickslate

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.search.domain.api.GenreRepository
import com.zsoltbertalan.flickslate.search.domain.api.model.GenreMoviesPagingReply
import com.zsoltbertalan.flickslate.shared.domain.model.GenreMother
import com.zsoltbertalan.flickslate.shared.domain.model.GenresReply
import com.zsoltbertalan.flickslate.base.kotlin.result.Failure
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@ViewModelScoped
class FakeGenreAccessor @Inject constructor() : GenreRepository {

	override fun getGenresList(): Flow<Outcome<GenresReply>> = flowOf(Ok(GenresReply(GenreMother.createGenreList())))

	override fun getGenreDetail(genreId: Int, page: Int): Flow<Outcome<GenreMoviesPagingReply>> = flowOf(
		Err(
			Failure.ServerError("Test error")
		)
	)

}
