package com.zsoltbertalan.flickslate.tv.data

import android.content.Context
import androidx.room.Room
import com.zsoltbertalan.flickslate.tv.data.db.TvDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TvDatabaseModule {

	@Provides
	@Singleton
	fun provideMoviesDatabase(@ApplicationContext context: Context) =
		Room.databaseBuilder(context, TvDatabase::class.java, "tvDatabase").build()

}
