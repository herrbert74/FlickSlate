package com.zsoltbertalan.flickslate.search.data

import android.content.Context
import androidx.room.Room
import com.zsoltbertalan.flickslate.search.data.db.SearchDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal class SearchDatabaseModule {

	@Provides
	@ViewModelScoped
	fun provideSearchDatabase(@ApplicationContext context: Context) =
		Room.databaseBuilder(context, SearchDatabase::class.java, "searchDatabase").build()
}
