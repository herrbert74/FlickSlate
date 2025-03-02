package com.zsoltbertalan.flickslate.search.ui.main

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.movies.domain.model.MovieMother
import com.zsoltbertalan.flickslate.search.domain.api.SearchRepository
import com.zsoltbertalan.flickslate.shared.model.Movie
import com.zsoltbertalan.flickslate.shared.model.PageData
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import dagger.hilt.android.scopes.ViewModelScoped
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ViewModelScoped
class FakeSearchRepository @Inject constructor(): SearchRepository {

	override suspend fun getSearchResult(query: String, page: Int): Outcome<PagingReply<Movie>> =
		Ok(PagingReply(MovieMother.createMovieList(), true, PageData()))

}