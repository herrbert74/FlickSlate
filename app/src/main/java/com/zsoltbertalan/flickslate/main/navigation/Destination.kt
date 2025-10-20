package com.zsoltbertalan.flickslate.main.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {

	@Serializable
	data object Movies : Destination

	@Serializable
	data object Tv : Destination

	@Serializable
	data object Search : Destination

	@Serializable
	data object Account : Destination

	@Serializable
	data class MovieDetails(val movieId: Int) : Destination

	@Serializable
	data class TvDetails(val seriesId: Int, val seasonNumber: Int? = null, val episodeNumber: Int? = null) : Destination

	@Serializable
	data class SeasonDetails(
		val seriesId: Int,
		val seasonNumber: Int,
		val bgColor: Int,
		val bgColorDim: Int,
		val episodeNumber: Int? = null,
	) : Destination

	@Serializable
	data class GenreMovies(val genreId: Int, val genreName: String) : Destination

}
