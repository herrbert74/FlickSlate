package com.zsoltbertalan.flickslate.account.data.repository

import com.zsoltbertalan.flickslate.account.domain.api.RatingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import javax.inject.Named

@Module(includes = [InternalRatingsRepositoryModule::class])
@InstallIn(ActivityRetainedComponent::class)
interface RatingsRepositoryModule {

	@Binds
	fun bindRatingsRepository(@Named("Internal") impl: RatingsRepository): RatingsRepository
}

@Module
@InstallIn(ActivityRetainedComponent::class)
internal interface InternalRatingsRepositoryModule {

	@Binds
	@Named("Internal")
	fun bindRatingsRepository(impl: RatingsAccessor): RatingsRepository

}
