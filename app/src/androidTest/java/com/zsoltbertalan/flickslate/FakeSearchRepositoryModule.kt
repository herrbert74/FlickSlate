package com.zsoltbertalan.flickslate

import com.zsoltbertalan.flickslate.search.data.repository.SearchRepositoryModule
import com.zsoltbertalan.flickslate.search.domain.api.GenreRepository
import com.zsoltbertalan.flickslate.search.domain.api.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
	replaces = [SearchRepositoryModule::class],
	components = [ViewModelComponent::class]
)
interface FakeSearchRepositoryModule {

	@Binds
	fun bindGenreRepository(impl: FakeGenreAccessor): GenreRepository

	@Binds
	fun bindSearchRepository(impl: FakeSearchRepository): SearchRepository

}
