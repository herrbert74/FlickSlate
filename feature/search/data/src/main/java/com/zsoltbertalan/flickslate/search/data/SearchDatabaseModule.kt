package com.zsoltbertalan.flickslate.search.data

import android.app.Application
import androidx.room.Room
import com.zsoltbertalan.flickslate.search.data.db.SearchDatabase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
interface SearchDatabaseModule {
	companion object {

		@Provides
		@SingleIn(AppScope::class)
		internal fun provideSearchDatabase(application: Application): SearchDatabase =
			Room.databaseBuilder(application, SearchDatabase::class.java, "searchDatabase").build()

	}
}
