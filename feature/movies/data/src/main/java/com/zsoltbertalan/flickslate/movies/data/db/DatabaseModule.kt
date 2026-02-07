package com.zsoltbertalan.flickslate.movies.data.db

import android.app.Application
import androidx.room.Room
import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(ActivityRetainedScope::class)
interface DatabaseModule {
	companion object {

		@Provides
		@SingleIn(ActivityRetainedScope::class)
		internal fun provideMoviesDatabase(application: Application): MoviesDatabase =
			Room.databaseBuilder(application, MoviesDatabase::class.java, "moviesDatabase")
				.addMigrations(MIGRATION_1_2)
				.build()

	}

}
