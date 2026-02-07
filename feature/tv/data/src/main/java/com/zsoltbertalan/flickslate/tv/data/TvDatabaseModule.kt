package com.zsoltbertalan.flickslate.tv.data

import android.app.Application
import androidx.room.Room
import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import com.zsoltbertalan.flickslate.tv.data.db.TvDatabase
import dagger.Module
import dagger.Provides
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.SingleIn

@Module
@ContributesTo(ActivityRetainedScope::class)
class TvDatabaseModule {

	@Provides
	@SingleIn(ActivityRetainedScope::class)
	fun provideMoviesDatabase(application: Application): TvDatabase =
		Room.databaseBuilder(application, TvDatabase::class.java, "tvDatabase").build()

}
