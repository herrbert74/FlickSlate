package com.zsoltbertalan.flickslate.search.data

import com.zsoltbertalan.flickslate.search.data.api.GenreDataSource
import com.zsoltbertalan.flickslate.search.data.api.GenreMoviesDataSource
import com.zsoltbertalan.flickslate.search.data.db.GenreDao
import com.zsoltbertalan.flickslate.search.data.db.GenreMoviesDao
import com.zsoltbertalan.flickslate.search.data.network.GenreMoviesRemoteDataSource
import com.zsoltbertalan.flickslate.search.data.network.GenreRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
interface SearchDataModule {

	@Binds
	fun bindGenreDataSource(genreDao: GenreDao): GenreDataSource.Local

	@Binds
	fun bindGenreRemoteDataSource(genreRemoteDataSource: GenreRemoteDataSource): GenreDataSource.Remote

	@Binds
	fun bindGenreMoviesDataSource(genreMoviesDao: GenreMoviesDao): GenreMoviesDataSource.Local

	@Binds
	fun bindGenreMoviesRemoteDataSource(
		genreMoviesRemoteDataSource: GenreMoviesRemoteDataSource
	): GenreMoviesDataSource.Remote

}
