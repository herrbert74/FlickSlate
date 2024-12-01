package com.zsoltbertalan.flickslate.search.data

import android.content.Context
import androidx.room.Room
import com.zsoltbertalan.flickslate.movies.data.api.NowPlayingMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.api.PopularMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.api.UpcomingMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.db.MoviesDatabase
import com.zsoltbertalan.flickslate.movies.data.network.NowPlayingRemoteDataSource
import com.zsoltbertalan.flickslate.movies.data.network.PopularMoviesRemoteDataSource
import com.zsoltbertalan.flickslate.movies.data.network.UpcomingMoviesRemoteDataSource
import com.zsoltbertalan.flickslate.search.data.db.SearchDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SearchDatabaseModule {

	@Provides
	@Singleton
	fun provideSearchDatabase(@ApplicationContext context: Context) =
		Room.databaseBuilder(context, SearchDatabase::class.java, "searchDatabase").build()
}
