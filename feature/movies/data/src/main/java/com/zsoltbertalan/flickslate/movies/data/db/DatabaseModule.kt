package com.zsoltbertalan.flickslate.movies.data.db

import android.app.Application
import androidx.room.Room
import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import dagger.Module
import dagger.Provides
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.SingleIn

@Module
@ContributesTo(ActivityRetainedScope::class)
internal class DatabaseModule {

	@Provides
	@SingleIn(ActivityRetainedScope::class)
	internal fun provideMoviesDatabase(application: Application) =
		Room.databaseBuilder(application, MoviesDatabase::class.java, "moviesDatabase")
			.addMigrations(MIGRATION_1_2)
			.build()

}
