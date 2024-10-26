package com.zsoltbertalan.flickslate.tv.data

import com.zsoltbertalan.flickslate.tv.data.api.TvDataSource
import com.zsoltbertalan.flickslate.tv.data.db.TvDao
import com.zsoltbertalan.flickslate.tv.data.network.TvRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
interface TvDataModule {

	@Binds
	fun bindTvDataSource(tvDao: TvDao): TvDataSource.Local

	@Binds
	fun bindTvRemoteDataSource(tvRemoteDataSource: TvRemoteDataSource): TvDataSource.Remote

}
