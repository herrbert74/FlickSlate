package com.zsoltbertalan.flickslate.movies.data.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class DatabaseModule {

	@Provides
	@ActivityRetainedScoped
	fun provideMoviesDatabase(@ApplicationContext context: Context) =
		Room.databaseBuilder(context, MoviesDatabase::class.java, "moviesDatabase").build()

}

