package com.zsoltbertalan.flickslate.account.data.network

import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import dagger.Module
import dagger.Provides
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.SingleIn
import retrofit2.Retrofit

@Module
@ContributesTo(ActivityRetainedScope::class)
internal class AccountServiceModule {

	@Provides
	@SingleIn(ActivityRetainedScope::class)
	fun provideAccountService(retroFit: Retrofit): AccountService {
		return retroFit.create(AccountService::class.java)
	}

}
