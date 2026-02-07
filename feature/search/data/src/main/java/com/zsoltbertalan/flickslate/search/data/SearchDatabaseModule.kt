package com.zsoltbertalan.flickslate.search.data

import android.app.Application
import androidx.room.Room
import com.zsoltbertalan.flickslate.search.data.db.SearchDatabase
import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(ActivityRetainedScope::class)
interface SearchDatabaseModule {
	companion object {

		@Provides
		@SingleIn(ActivityRetainedScope::class)
		fun provideSearchDatabase(application: Application): SearchDatabase =
			Room.databaseBuilder(application, SearchDatabase::class.java, "searchDatabase").build()

	}
}
