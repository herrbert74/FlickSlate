package com.zsoltbertalan.flickslate.movies.data.db

import com.zsoltbertalan.flickslate.movies.data.api.NowPlayingMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.api.PopularMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.api.UpcomingMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.network.NowPlayingRemoteDataSource
import com.zsoltbertalan.flickslate.movies.data.network.PopularMoviesRemoteDataSource
import com.zsoltbertalan.flickslate.movies.data.network.UpcomingMoviesRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
interface MoviesDatabaseModule {

	@Binds
	fun bindPopularMoviesDataSource(popularMoviesDao: PopularMoviesDao): PopularMoviesDataSource.Local

	@Binds
	fun bindUpcomingMoviesDataSource(upcomingMoviesDao: UpcomingMoviesDao): UpcomingMoviesDataSource.Local

	@Binds
	fun bindNowPlayingMoviesDataSource(nowPlayingMoviesDao: NowPlayingMoviesDao): NowPlayingMoviesDataSource.Local

	@Binds
	fun bindNowPlayingMoviesRemoteDataSource(
		nowPlayingRemoteDataSource: NowPlayingRemoteDataSource
	): NowPlayingMoviesDataSource.Remote

	@Binds
	fun bindPopularMoviesRemoteDataSource(
		popularRemoteDataSource: PopularMoviesRemoteDataSource
	): PopularMoviesDataSource.Remote

	@Binds
	fun bindUpcomingMoviesRemoteDataSource(
		upcomingRemoteDataSource: UpcomingMoviesRemoteDataSource
	): UpcomingMoviesDataSource.Remote

}
