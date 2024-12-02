package com.zsoltbertalan.flickslate.movies.data.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

	@Provides
	@Singleton
	fun provideMoviesDatabase(@ApplicationContext context: Context) =
		Room.databaseBuilder(context, MoviesDatabase::class.java, "moviesDatabase").build()

}

