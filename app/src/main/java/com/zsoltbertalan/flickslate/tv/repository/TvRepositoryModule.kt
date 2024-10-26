package com.zsoltbertalan.flickslate.tv.repository

import com.zsoltbertalan.flickslate.tv.domain.api.TvRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
interface TvRepositoryModule {

	@Binds
	@Singleton
	fun bindTvRepository(tvAccessor: TvAccessor): TvRepository

}
