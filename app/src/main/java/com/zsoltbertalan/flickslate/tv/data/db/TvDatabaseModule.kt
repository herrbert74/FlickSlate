package com.zsoltbertalan.flickslate.tv.data.db

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface TvDatabaseModule {

	@Binds
	fun bindTvDataSource(tvDao: TvDao): TvDataSource

}
