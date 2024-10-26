package com.zsoltbertalan.flickslate.movies.repository

import com.zsoltbertalan.flickslate.movies.domain.api.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
interface MoviesRepositoryModule {

	@Binds
	@Singleton
	fun bindMoviesRepository(moviesAccessor: MoviesAccessor): MoviesRepository

}
