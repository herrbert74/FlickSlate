package com.zsoltbertalan.flickslate.search.ui.main

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.search.domain.api.GenreRepository
import com.zsoltbertalan.flickslate.search.domain.api.model.GenreMoviesPagingReply
import com.zsoltbertalan.flickslate.shared.model.Failure
import com.zsoltbertalan.flickslate.shared.model.GenreMother
import com.zsoltbertalan.flickslate.shared.model.GenresReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ViewModelScoped
class FakeGenreAccessor @Inject constructor() : GenreRepository {

	override fun getGenresList(): Flow<Outcome<GenresReply>> = flowOf(Ok(GenresReply(GenreMother.createGenreList())))

	override fun getGenreDetail(genreId: Int, page: Int): Flow<Outcome<GenreMoviesPagingReply>> = flowOf(
		Err(
			Failure.ServerError("Test error")
		)
	)

}
