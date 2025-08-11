package com.zsoltbertalan.flickslate.movies.domain.usecase

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.get
import com.github.michaelbull.result.map
import com.github.michaelbull.result.recover
import com.zsoltbertalan.flickslate.movies.domain.api.MoviesRepository
import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetail
import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetailWithImages
import com.zsoltbertalan.flickslate.shared.domain.model.images.ImagesReply
import com.zsoltbertalan.flickslate.shared.kotlin.result.Failure
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class MovieDetailsUseCase @Inject constructor(private val moviesRepository: MoviesRepository) {

	suspend fun getMovieDetails(movieId: Int): Outcome<MovieDetailWithImages> {
		return coroutineScope {
			val movieDetails = async { moviesRepository.getMovieDetails(movieId) }
			val movieImages = async { moviesRepository.getMovieImages(movieId) }

			return@coroutineScope movieDetailWithImages(movieDetails.await(), movieImages.await())
		}
	}

	/**
	 * Return movie details if the movie details are available, an error otherwise.
	 *
	 * Attach images or an empty list to it depending on the images outcome.
	 */
	private fun movieDetailWithImages(
		movieDetails: Result<MovieDetail, Failure>,
		movieImages: Result<ImagesReply, Failure>
	): Outcome<MovieDetailWithImages> {
		return movieDetails.map { movieDetail ->
			val images = movieImages.recover { ImagesReply(emptyList(), emptyList(), emptyList(), 0) }
			MovieDetailWithImages(
				id = movieDetail.id,
				title = movieDetail.title,
				overview = movieDetail.overview,
				voteAverage = movieDetail.voteAverage,
				posterPath = movieDetail.posterPath,
				backdropPath = movieDetail.backdropPath,
				genres = movieDetail.genres,
				movieImages = images.get() ?: ImagesReply()
			)
		}
	}

}
