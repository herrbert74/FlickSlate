package com.zsoltbertalan.flickslate.shared.data.db

import com.zsoltbertalan.flickslate.movies.data.db.model.NowPlayingMovieDbo
import com.zsoltbertalan.flickslate.movies.data.db.model.NowPlayingMoviesPageDbo
import com.zsoltbertalan.flickslate.movies.data.db.model.PopularMovieDbo
import com.zsoltbertalan.flickslate.movies.data.db.model.PopularMoviesPageDbo
import com.zsoltbertalan.flickslate.movies.data.db.model.UpcomingMovieDbo
import com.zsoltbertalan.flickslate.movies.data.db.model.UpcomingMoviesPageDbo
import com.zsoltbertalan.flickslate.search.data.db.model.EtagDbo
import com.zsoltbertalan.flickslate.search.data.db.model.GenreDbo
import com.zsoltbertalan.flickslate.search.data.db.model.GenreMovieDbo
import com.zsoltbertalan.flickslate.search.data.db.model.GenreMoviesPageDbo
import com.zsoltbertalan.flickslate.tv.data.db.model.TvPageDbo
import com.zsoltbertalan.flickslate.tv.data.db.model.TvShowDbo
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

