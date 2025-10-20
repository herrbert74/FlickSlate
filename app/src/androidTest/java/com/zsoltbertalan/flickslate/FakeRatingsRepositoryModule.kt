package com.zsoltbertalan.flickslate

import com.zsoltbertalan.flickslate.account.data.repository.RatingsRepositoryModule
import com.zsoltbertalan.flickslate.account.domain.api.RatingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
	replaces = [RatingsRepositoryModule::class],
	components = [ActivityRetainedComponent::class]
)
interface FakeRatingsRepositoryModule {

	@Binds
	fun bindRatingsRepository(impl: FakeRatingsRepository): RatingsRepository

}
