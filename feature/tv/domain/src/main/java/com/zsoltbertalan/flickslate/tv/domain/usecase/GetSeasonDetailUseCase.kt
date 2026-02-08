package com.zsoltbertalan.flickslate.tv.domain.usecase

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.tv.domain.api.TvRepository
import com.zsoltbertalan.flickslate.tv.domain.model.SeasonDetail
import dev.zacsweers.metro.Inject

class GetSeasonDetailUseCase @Inject internal constructor(
	private val tvRepository: TvRepository
) {

	suspend fun execute(seriesId: Int, seasonNumber: Int): Outcome<SeasonDetail> {
		return tvRepository.getTvSeasonDetail(seriesId = seriesId, seasonNumber = seasonNumber)
	}

}
