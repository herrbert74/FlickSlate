package com.zsoltbertalan.flickslate

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.shared.model.PageData
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.testhelper.TvMother
import com.zsoltbertalan.flickslate.tv.domain.api.TvRepository
import com.zsoltbertalan.flickslate.tv.data.repository.TvRepositoryModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [TvRepositoryModule::class])
class MockTvRepositoryModule {

	@Provides
	@Singleton
	fun provideTvRepository(): TvRepository {
		return mockk {
			val pagingData = PagingReply(TvMother.createTvList(), true, PageData())
			coEvery { getTopRatedTv(any()) } returns flowOf(Ok(pagingData))
		}
	}

}
