package com.zsoltbertalan.flickslate

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.search.data.repository.SearchAccessor
import com.zsoltbertalan.flickslate.search.domain.api.SearchRepository
import com.zsoltbertalan.flickslate.shared.model.GenresReply
import com.zsoltbertalan.flickslate.shared.model.Movie
import com.zsoltbertalan.flickslate.shared.model.PageData
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import com.zsoltbertalan.flickslate.testhelper.GenreMother
import com.zsoltbertalan.flickslate.testhelper.MovieMother
import com.zsoltbertalan.flickslate.testhelper.TvMother
import com.zsoltbertalan.flickslate.tv.data.repository.TvAccessor
import com.zsoltbertalan.flickslate.tv.domain.api.TvRepository
import com.zsoltbertalan.flickslate.tv.domain.model.TvDetail
import com.zsoltbertalan.flickslate.tv.domain.model.TvShow
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import se.ansman.dagger.auto.android.testing.Replaces
import javax.inject.Inject

@Replaces(SearchAccessor::class)
@ViewModelScoped
class FakeSearchRepository @Inject constructor(): SearchRepository {

	override suspend fun getSearchResult(query: String, page: Int): Outcome<PagingReply<Movie>> =
		Ok(PagingReply(MovieMother.createMovieList(), true, PageData()))

}
