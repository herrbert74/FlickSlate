package com.zsoltbertalan.flickslate.search.repository

import com.zsoltbertalan.flickslate.search.domain.api.GenreRepository
import com.zsoltbertalan.flickslate.search.domain.api.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
interface SearchRepositoryModule {

	@Binds
	@Singleton
	fun bindGenreRepository(genreAccessor: GenreAccessor): GenreRepository

	@Binds
	@Singleton
	fun bindSearchRepository(searchAccessor: SearchAccessor): SearchRepository

}

