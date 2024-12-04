package com.zsoltbertalan.flickslate.search.data

import android.content.Context
import androidx.room.Room
import com.zsoltbertalan.flickslate.search.data.db.SearchDatabase
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
