package com.zsoltbertalan.flickslate.tv.domain.usecase

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.get
import com.github.michaelbull.result.map
import com.github.michaelbull.result.recover
import com.zsoltbertalan.flickslate.account.domain.usecase.GetSessionIdUseCase
import com.zsoltbertalan.flickslate.base.kotlin.result.Failure
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.shared.domain.model.images.ImagesReply
import com.zsoltbertalan.flickslate.tv.domain.api.TvRepository
import com.zsoltbertalan.flickslate.tv.domain.model.TvDetail
import com.zsoltbertalan.flickslate.tv.domain.model.TvDetailWithImages
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class TvDetailsUseCase @Inject internal constructor(
	private val tvRepository: TvRepository,
	private val getSessionIdUseCase: GetSessionIdUseCase,
) {

	suspend fun getTvDetails(tvId: Int): Outcome<TvDetailWithImages> {
		return coroutineScope {
			val sessionId = async { getSessionIdUseCase.execute() }
			val tvDetails = async { tvRepository.getTvDetails(tvId, sessionId.await().get()) }
			val tvImages = async { tvRepository.getTvImages(tvId) }

			return@coroutineScope tvDetailWithImages(tvDetails.await(), tvImages.await())
		}
	}

	/**
	 * Return TV series details if the TV details are available, an error otherwise.
	 *
	 * Attach images or an empty list to it depending on the images outcome.
	 */
	private fun tvDetailWithImages(
		tvDetails: Result<TvDetail, Failure>,
		tvImages: Result<ImagesReply, Failure>
	): Outcome<TvDetailWithImages> {
		return tvDetails.map { tvDetail ->
			val images = tvImages.recover { ImagesReply(emptyList(), emptyList(), emptyList(), 0) }
			TvDetailWithImages(
				id = tvDetail.id,
				title = tvDetail.title,
				overview = tvDetail.overview,
				voteAverage = tvDetail.voteAverage,
				posterPath = tvDetail.posterPath,
				backdropPath = tvDetail.backdropPath,
				genres = tvDetail.genres,
				seasons = tvDetail.seasons,
				tvImages = images.get() ?: ImagesReply(),
				status = tvDetail.status,
				tagline = tvDetail.tagline,
				tvDetail.years,
				personalRating = tvDetail.personalRating,
				favorite = tvDetail.favorite,
			)
		}
	}

}
