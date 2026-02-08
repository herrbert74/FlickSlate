package com.zsoltbertalan.flickslate.tv.data

import android.app.Application
import androidx.room.Room
import com.zsoltbertalan.flickslate.tv.data.db.TvDatabase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
interface TvDatabaseModule {

	@Provides
	@SingleIn(AppScope::class)
	fun provideMoviesDatabase(application: Application): TvDatabase =
		Room.databaseBuilder(application, TvDatabase::class.java, "tvDatabase").build()

}
