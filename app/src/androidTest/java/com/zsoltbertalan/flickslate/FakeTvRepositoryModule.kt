package com.zsoltbertalan.flickslate

import com.zsoltbertalan.flickslate.tv.data.repository.TvRepositoryModule
import com.zsoltbertalan.flickslate.tv.domain.api.TvRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
	replaces = [TvRepositoryModule::class],
	components = [ViewModelComponent::class]
)
interface FakeTvRepositoryModule {

	@Binds
	fun bindTvRepository(impl: FakeTvRepository): TvRepository

}
