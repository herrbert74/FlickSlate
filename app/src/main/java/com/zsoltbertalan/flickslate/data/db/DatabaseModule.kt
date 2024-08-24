package com.zsoltbertalan.flickslate.data.db

import com.zsoltbertalan.flickslate.common.async.IoDispatcher
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.CoroutineDispatcher
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
			GenreMoviesDbo::class,
			GenreMoviesPageDbo::class,
			PopularMoviesDbo::class,
			PopularMoviesPageDbo::class,
			NowPlayingMoviesDbo::class,
			NowPlayingMoviesPageDbo::class,
			UpcomingMoviesDbo::class,
			UpcomingMoviesPageDbo::class,
			TvDbo::class,
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
