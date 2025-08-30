package com.zsoltbertalan.flickslate.movies.data.repository

import com.zsoltbertalan.flickslate.movies.domain.api.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import javax.inject.Named

/**
 * This technique is used to hide the implementation of the only public interface (and all of its dependencies),
 * but still able to override it in tests with Hilt.
 * For this, there is an internal binding wrapped in a public binding module.
 * AutoBind cannot used for this as it works by binding, while Hilt works by module.
 * The Internal qualifier is needed to avoid multiple bindings in tests. This way we only need to override the
 * unqualified visible implementation in tests.
 */

@Module(includes = [InternalMoviesRepositoryModule::class])
@InstallIn(ActivityRetainedComponent::class)
interface MoviesRepositoryModule {

	@Binds
	fun bindMoviesRepository(@Named("Internal") impl: MoviesRepository): MoviesRepository
}

@Module
@InstallIn(ActivityRetainedComponent::class)
internal interface InternalMoviesRepositoryModule {

	@Binds
	@Named("Internal")
	fun bindMoviesRepository(impl: MoviesAccessor): MoviesRepository

}
