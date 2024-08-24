package com.zsoltbertalan.flickslate.data.db

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

	@Provides
	@Singleton
	fun provideRealmConfiguration() = RealmConfiguration.Builder(
		schema = setOf(
			GenreDbo::class,
			EtagDbo::class,
			GenreMovieDbo::class,
			GenreMoviesPageDbo::class,
			PopularMovieDbo::class,
			PopularMoviesPageDbo::class,
			NowPlayingMovieDbo::class,
			NowPlayingMoviesPageDbo::class,
			UpcomingMovieDbo::class,
			UpcomingMoviesPageDbo::class,
			TvShowDbo::class,
			TvPageDbo::class,
		)
	).build()

	@Provides
	@Singleton
	fun provideRealm(realmConfiguration: RealmConfiguration) = Realm.open(realmConfiguration)

}

@Module
@InstallIn(SingletonComponent::class)
interface BindingDatabaseModule {

	@Binds
	fun bindGenreDataSource(genreDao: GenreDao): GenreDataSource

	@Binds
	fun bindGenreMoviesDataSource(genreMoviesDao: GenreMoviesDao): GenreMoviesDataSource

	@Binds
	fun bindTvDataSource(tvDao: TvDao): TvDataSource

	@Binds
	fun bindPopularMoviesDataSource(popularMoviesDao: PopularMoviesDao): PopularMoviesDataSource

	@Binds
	fun bindUpcomingMoviesDataSource(upcomingMoviesDao: UpcomingMoviesDao): UpcomingMoviesDataSource

	@Binds
	fun bindNowPlayingMoviesDataSource(nowPlayingMoviesDao: NowPlayingMoviesDao): NowPlayingMoviesDataSource
}
