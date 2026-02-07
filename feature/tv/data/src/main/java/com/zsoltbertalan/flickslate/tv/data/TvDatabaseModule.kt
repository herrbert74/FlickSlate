package com.zsoltbertalan.flickslate.tv.data

import android.app.Application
import androidx.room.Room
import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import com.zsoltbertalan.flickslate.tv.data.db.TvDatabase
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(ActivityRetainedScope::class)
interface TvDatabaseModule {
	companion object {

		@Provides
		@SingleIn(ActivityRetainedScope::class)
		fun provideMoviesDatabase(application: Application): TvDatabase =
			Room.databaseBuilder(application, TvDatabase::class.java, "tvDatabase").build()

	}

}
