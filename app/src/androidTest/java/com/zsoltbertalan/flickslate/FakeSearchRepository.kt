package com.zsoltbertalan.flickslate

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.movies.domain.model.MovieMother
import com.zsoltbertalan.flickslate.search.data.repository.SearchAccessor
import com.zsoltbertalan.flickslate.search.domain.api.SearchRepository
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import dagger.hilt.android.scopes.ViewModelScoped
import se.ansman.dagger.auto.android.testing.Replaces
import javax.inject.Inject

@Replaces(SearchAccessor::class)
@ViewModelScoped
class FakeSearchRepository @Inject constructor() : SearchRepository {

	override suspend fun getSearchResult(query: String, page: Int): Outcome<PagingReply<Movie>> =
		Ok(PagingReply(MovieMother.createMovieList(), true, PageData()))

}
