package com.zsoltbertalan.flickslate.search.data

import android.app.Application
import androidx.room.Room
import com.zsoltbertalan.flickslate.search.data.db.SearchDatabase
import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import dagger.Module
import dagger.Provides
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.SingleIn

@Module
@ContributesTo(ActivityRetainedScope::class)
internal class SearchDatabaseModule {

	@Provides
	@SingleIn(ActivityRetainedScope::class)
	fun provideSearchDatabase(application: Application) =
		Room.databaseBuilder(application, SearchDatabase::class.java, "searchDatabase").build()
}
