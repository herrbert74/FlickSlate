package com.zsoltbertalan.flickslate

import com.zsoltbertalan.flickslate.movies.data.repository.MoviesRepositoryModule
import com.zsoltbertalan.flickslate.movies.domain.api.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
	replaces = [MoviesRepositoryModule::class],
	components = [ActivityRetainedComponent::class]
)
interface FakeMoviesRepositoryModule {

	@Binds
	fun bindMoviesRepository(impl: FakeMoviesRepository): MoviesRepository

}
