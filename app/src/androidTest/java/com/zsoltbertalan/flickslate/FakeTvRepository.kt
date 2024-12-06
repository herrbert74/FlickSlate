package com.zsoltbertalan.flickslate

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.shared.model.PageData
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
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

@Replaces(TvAccessor::class)
@ViewModelScoped
class FakeTvRepository @Inject constructor(): TvRepository {

	override fun getTopRatedTv(page: Int): Flow<Outcome<PagingReply<TvShow>>> =
		flowOf(Ok(PagingReply(TvMother.createTvList(), true, PageData())))

	override suspend fun getTvDetails(seriesId: Int): Outcome<TvDetail> = Ok(TvMother.createTvDetail())

}
