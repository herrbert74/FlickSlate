package com.zsoltbertalan.flickslate.search.data.repository

import com.zsoltbertalan.flickslate.search.domain.api.GenreRepository
import com.zsoltbertalan.flickslate.search.domain.api.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Named

/**
 * This technique is used to hide the implementation of the only public interface (and all of its dependencies),
 * but still able to override it in tests with Hilt.
 * For this, there is an internal binding wrapped in a public binding module.
 * AutoBind cannot used for this as it works by binding, while Hilt works by module.
 * The Internal qualifier is needed to avoid multiple bindings in tests. This way we only need to override the
 * unqualified visible implementation in tests.
 */

@Module(includes = [InternalSearchRepositoryModule::class])
@InstallIn(ViewModelComponent::class)
interface SearchRepositoryModule {

	@Binds
	fun bindGenreRepository(@Named("Internal") impl: GenreRepository): GenreRepository

	@Binds
	fun bindSearchRepository(@Named("Internal") impl: SearchRepository): SearchRepository
}

@Module
@InstallIn(ViewModelComponent::class)
internal interface InternalSearchRepositoryModule {

	@Binds
	@Named("Internal")
	fun bindGenreRepository(impl: GenreAccessor): GenreRepository

	@Binds
	@Named("Internal")
	fun bindSearchRepository(impl: SearchAccessor): SearchRepository

}
