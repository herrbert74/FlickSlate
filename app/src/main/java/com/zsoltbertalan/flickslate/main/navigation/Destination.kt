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
	data class TvDetails(val seriesId: Int) : Destination

	@Serializable
	data class GenreMovies(val genreId: Int, val genreName: String) : Destination

}
