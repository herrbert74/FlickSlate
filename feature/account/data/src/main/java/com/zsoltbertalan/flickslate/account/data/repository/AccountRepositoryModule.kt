package com.zsoltbertalan.flickslate.account.data.repository

import com.zsoltbertalan.flickslate.account.domain.api.AccountRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import javax.inject.Named

@Module(includes = [InternalAccountRepositoryModule::class])
@InstallIn(ActivityRetainedComponent::class)
interface AccountRepositoryModule {

	@Binds
	fun bindAccountRepository(@Named("Internal") impl: AccountRepository): AccountRepository
}

@Module
@InstallIn(ActivityRetainedComponent::class)
internal interface InternalAccountRepositoryModule {

	@Binds
	@Named("Internal")
	fun bindAccountRepository(impl: AccountAccessor): AccountRepository

}
