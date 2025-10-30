package com.zsoltbertalan.flickslate.account.domain.model

import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.tv.domain.model.TvMother

object RatedTvMother {

	fun createRatedTvList(): PagingReply<RatedTvShow> {
		return PagingReply(
			listOf(RatedTvShow(tvShow = TvMother.createTvList()[0], rating = 7f)),
			isLastPage = true,
			PageData()
		)
	}

	fun createRatedEpisodeList(): PagingReply<RatedTvEpisode> {
		return PagingReply(
			listOf(
				RatedTvEpisode(
					tvEpisodeDetail = TvMother.createSeasonDetail(0, 0).episodes[0],
					rating = 7f
				),
			),
			isLastPage = true,
			PageData()
		)
	}

}
