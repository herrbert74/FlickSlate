package com.zsoltbertalan.flickslate.movies.data.db

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface MoviesDatabaseModule {

	@Binds
	fun bindPopularMoviesDataSource(popularMoviesDao: PopularMoviesDao): PopularMoviesDataSource

	@Binds
	fun bindUpcomingMoviesDataSource(upcomingMoviesDao: UpcomingMoviesDao): UpcomingMoviesDataSource

	@Binds
	fun bindNowPlayingMoviesDataSource(nowPlayingMoviesDao: NowPlayingMoviesDao): NowPlayingMoviesDataSource

}
