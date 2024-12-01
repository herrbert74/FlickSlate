package com.zsoltbertalan.flickslate.search.data

import com.zsoltbertalan.flickslate.search.data.api.GenreDataSource
import com.zsoltbertalan.flickslate.search.data.api.GenreMoviesDataSource
import com.zsoltbertalan.flickslate.search.data.db.GenreMoviesRoomDataSource
import com.zsoltbertalan.flickslate.search.data.db.GenresRoomDataSource
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
	fun bindGenreDataSource(genreLocalDataSource: GenresRoomDataSource): GenreDataSource.Local

	@Binds
	fun bindGenreRemoteDataSource(genreRemoteDataSource: GenreRemoteDataSource): GenreDataSource.Remote

	@Binds
	fun bindGenreMoviesDataSource(genreMoviesDao: GenreMoviesRoomDataSource): GenreMoviesDataSource.Local

	@Binds
	fun bindGenreMoviesRemoteDataSource(
		genreMoviesRemoteDataSource: GenreMoviesRemoteDataSource
	): GenreMoviesDataSource.Remote

}
