package com.zsoltbertalan.flickslate

import com.zsoltbertalan.flickslate.account.data.repository.AccountRepositoryModule
import com.zsoltbertalan.flickslate.account.domain.api.AccountRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
	replaces = [AccountRepositoryModule::class],
	components = [ActivityRetainedComponent::class]
)
interface FakeAccountRepositoryModule {

	@Binds
	fun bindAccountRepository(impl: FakeAccountRepository): AccountRepository

}
