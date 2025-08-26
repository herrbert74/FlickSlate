package com.zsoltbertalan.flickslate.tv.domain.usecase

import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.tv.domain.api.TvRepository
import com.zsoltbertalan.flickslate.tv.domain.model.SeasonDetail
import javax.inject.Inject

class GetSeasonDetailUseCase @Inject constructor(
	private val tvRepository: TvRepository
) {

	suspend fun execute(seriesId: Int, seasonNumber: Int): Outcome<SeasonDetail> {
		return tvRepository.getTvSeasonDetail(seriesId = seriesId, seasonNumber = seasonNumber)
	}

}
