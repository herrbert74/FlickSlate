package com.zsoltbertalan.flickslate.search.data.db

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SearchDatabaseModule {

	@Binds
	fun bindGenreDataSource(genreDao: GenreDao): GenreDataSource

	@Binds
	fun bindGenreMoviesDataSource(genreMoviesDao: GenreMoviesDao): GenreMoviesDataSource

}
