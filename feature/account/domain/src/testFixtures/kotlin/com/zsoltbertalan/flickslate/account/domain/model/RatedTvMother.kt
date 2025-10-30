package com.zsoltbertalan.flickslate.account.domain.model

import com.zsoltbertalan.flickslate.tv.domain.model.TvMother

object RatedTvMother {

	fun createRatedTvList(): List<RatedTvShow> {
		return listOf(
			RatedTvShow(tvShow = TvMother.createTvList()[0], rating = 7f),
		)
	}

	fun createRatedEpisodeList(): List<RatedTvEpisode> {
		return listOf(
			RatedTvEpisode(tvEpisodeDetail = TvMother.createSeasonDetail(0, 0).episodes[0], rating = 7f),
		)
	}

}
