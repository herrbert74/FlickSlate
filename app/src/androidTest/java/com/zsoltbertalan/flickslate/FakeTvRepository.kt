package com.zsoltbertalan.flickslate

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.domain.model.images.ImagesReply
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.tv.domain.api.TvRepository
import com.zsoltbertalan.flickslate.tv.domain.model.SeasonDetail
import com.zsoltbertalan.flickslate.tv.domain.model.TvDetail
import com.zsoltbertalan.flickslate.tv.domain.model.TvMother
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@ViewModelScoped
class FakeTvRepository @Inject constructor() : TvRepository {

	override fun getTopRatedTv(page: Int): Flow<Outcome<PagingReply<TvShow>>> =
		flowOf(Ok(PagingReply(TvMother.createTvList(), true, PageData())))

	override suspend fun getTvDetails(seriesId: Int): Outcome<TvDetail> = Ok(TvMother.createTvDetail())

	override suspend fun getTvImages(seriesId: Int): Outcome<ImagesReply> = Ok(TvMother.createTvImages())

	override suspend fun getTvSeasonDetail(seriesId: Int, seasonNumber: Int): Outcome<SeasonDetail> =
		Ok(TvMother.createSeasonDetail(seriesId, seasonNumber))

}
