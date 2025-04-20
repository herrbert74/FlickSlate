package com.zsoltbertalan.flickslate.tv.domain.usecase

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.get
import com.github.michaelbull.result.map
import com.github.michaelbull.result.recover
import com.zsoltbertalan.flickslate.shared.model.Failure
import com.zsoltbertalan.flickslate.shared.model.images.ImagesReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import com.zsoltbertalan.flickslate.tv.domain.api.TvRepository
import com.zsoltbertalan.flickslate.tv.domain.model.TvDetail
import com.zsoltbertalan.flickslate.tv.domain.model.TvDetailWithImages
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class TvDetailsUseCase @Inject constructor(private val tvRepository: TvRepository) {

	suspend fun getTvDetails(tvId: Int): Outcome<TvDetailWithImages> {
		return coroutineScope {
			val tvDetails = async { tvRepository.getTvDetails(tvId) }
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
				tvImages = images.get() ?: ImagesReply()
			)
		}
	}

}
