package com.zsoltbertalan.flickslate

import com.zsoltbertalan.flickslate.account.domain.api.AccountRepository
import com.zsoltbertalan.flickslate.account.domain.api.FavoritesRepository
import com.zsoltbertalan.flickslate.account.domain.api.RatingsRepository
import com.zsoltbertalan.flickslate.movies.domain.api.MovieFavoritesRepository
import com.zsoltbertalan.flickslate.movies.domain.api.MovieRatingsRepository
import com.zsoltbertalan.flickslate.movies.domain.api.MoviesRepository
import com.zsoltbertalan.flickslate.search.domain.api.GenreRepository
import com.zsoltbertalan.flickslate.search.domain.api.SearchRepository
import com.zsoltbertalan.flickslate.tv.domain.api.TvFavoritesRepository
import com.zsoltbertalan.flickslate.tv.domain.api.TvRatingsRepository
import com.zsoltbertalan.flickslate.tv.domain.api.TvRepository
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides

@BindingContainer
class TestOverrides {

	val fakeAccountRepository = FakeAccountRepository()
	val fakeMoviesRepository = FakeMoviesRepository()
	val fakeTvRepository = FakeTvRepository()
	val fakeSearchRepository = FakeSearchRepository()
	val fakeGenreAccessor = FakeGenreAccessor()
	val fakeRatingsRepository = FakeRatingsRepository()
	val fakeMovieRatingsRepository = FakeMovieRatingsRepository()
	val fakeTvRatingsRepository = FakeTvRatingsRepository()
	val fakeMovieFavoritesRepository = FakeMovieFavoritesRepository()
	val fakeTvFavoritesRepository = FakeTvFavoritesRepository()
	val fakeFavoritesRepository = FakeFavoritesRepository()

	@Provides
	fun accountRepository(): AccountRepository = fakeAccountRepository

	@Provides
	fun moviesRepository(): MoviesRepository = fakeMoviesRepository

	@Provides
	fun tvRepository(): TvRepository = fakeTvRepository

	@Provides
	fun searchRepository(): SearchRepository = fakeSearchRepository

	@Provides
	fun genreRepository(): GenreRepository = fakeGenreAccessor

	@Provides
	fun ratingsRepository(): RatingsRepository = fakeRatingsRepository

	@Provides
	fun movieRatingsRepository(): MovieRatingsRepository = fakeMovieRatingsRepository

	@Provides
	fun tvRatingsRepository(): TvRatingsRepository = fakeTvRatingsRepository

	@Provides
	fun movieFavoritesRepository(): MovieFavoritesRepository = fakeMovieFavoritesRepository

	@Provides
	fun tvFavoritesRepository(): TvFavoritesRepository = fakeTvFavoritesRepository

	@Provides
	fun favoritesRepository(): FavoritesRepository = fakeFavoritesRepository
}
