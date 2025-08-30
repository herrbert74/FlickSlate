package com.zsoltbertalan.flickslate.tv.data.repository

import com.zsoltbertalan.flickslate.tv.domain.api.TvRepository
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

@Module(includes = [InternalTvRepositoryModule::class])
@InstallIn(ViewModelComponent::class)
interface TvRepositoryModule {

	@Binds
	fun bindTvRepository(@Named("Internal") impl: TvRepository): TvRepository
}

@Module
@InstallIn(ViewModelComponent::class)
internal interface InternalTvRepositoryModule {

	@Binds
	@Named("Internal")
	fun bindTvRepository(impl: TvAccessor): TvRepository

}
