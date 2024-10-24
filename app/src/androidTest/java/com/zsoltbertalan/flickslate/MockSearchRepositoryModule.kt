package com.zsoltbertalan.flickslate

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.search.domain.api.GenreRepository
import com.zsoltbertalan.flickslate.search.domain.api.SearchRepository
import com.zsoltbertalan.flickslate.search.repository.SearchRepositoryModule
import com.zsoltbertalan.flickslate.shared.domain.model.GenresReply
import com.zsoltbertalan.flickslate.shared.testhelper.GenreMother
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [SearchRepositoryModule::class])
class MockSearchRepositoryModule {

	@Provides
	@Singleton
	fun provideGenreRepository(): GenreRepository {
		return mockk {
			coEvery { getGenresList() } returns flowOf(Ok(GenresReply( GenreMother.createGenreList())))
		}
	}

	@Provides
	@Singleton
	fun provideSearchRepository(): SearchRepository {
		return mockk {

		}
	}

}
