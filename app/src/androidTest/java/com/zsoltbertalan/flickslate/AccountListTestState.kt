package com.zsoltbertalan.flickslate

import java.util.concurrent.ConcurrentHashMap

internal object AccountListTestState {

	private val favoriteMovieIdsInternal = ConcurrentHashMap.newKeySet<Int>()
	private val favoriteTvShowIdsInternal = ConcurrentHashMap.newKeySet<Int>()
	private val ratedMovieRatingsInternal = ConcurrentHashMap<Int, Float>()
	private val ratedTvShowRatingsInternal = ConcurrentHashMap<Int, Float>()

	@Volatile
	var includeRatedTvEpisode: Boolean = true
		private set

	val favoriteMovieIds: Set<Int>
		get() = favoriteMovieIdsInternal

	val favoriteTvShowIds: Set<Int>
		get() = favoriteTvShowIdsInternal

	val ratedMovieRatings: Map<Int, Float>
		get() = ratedMovieRatingsInternal

	val ratedTvShowRatings: Map<Int, Float>
		get() = ratedTvShowRatingsInternal

	fun resetToDefaults() {
		favoriteMovieIdsInternal.clear()
		favoriteTvShowIdsInternal.clear()
		ratedMovieRatingsInternal.clear()
		ratedTvShowRatingsInternal.clear()

		favoriteMovieIdsInternal.add(1)
		favoriteTvShowIdsInternal.add(2)

		ratedMovieRatingsInternal[0] = 10f
		ratedTvShowRatingsInternal[0] = 7f
		includeRatedTvEpisode = true
	}

	fun setMovieFavorite(movieId: Int, favorite: Boolean) {
		if (favorite) {
			favoriteMovieIdsInternal.add(movieId)
		} else {
			favoriteMovieIdsInternal.remove(movieId)
		}
	}

	fun setTvShowFavorite(tvShowId: Int, favorite: Boolean) {
		if (favorite) {
			favoriteTvShowIdsInternal.add(tvShowId)
		} else {
			favoriteTvShowIdsInternal.remove(tvShowId)
		}
	}

	fun setMovieRating(movieId: Int, rating: Float) {
		if (rating >= 0f) {
			ratedMovieRatingsInternal[movieId] = rating
		} else {
			ratedMovieRatingsInternal.remove(movieId)
		}
	}

	fun setTvShowRating(tvShowId: Int, rating: Float) {
		if (rating >= 0f) {
			ratedTvShowRatingsInternal[tvShowId] = rating
		} else {
			ratedTvShowRatingsInternal.remove(tvShowId)
		}
	}

	fun setIncludeRatedTvEpisode(include: Boolean) {
		includeRatedTvEpisode = include
	}
}
