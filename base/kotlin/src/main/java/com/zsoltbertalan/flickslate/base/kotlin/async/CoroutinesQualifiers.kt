package com.zsoltbertalan.flickslate.base.kotlin.async

import javax.inject.Qualifier

/**
 * These classes are provided in the base.android module to keep this module free from Hilt and Android dependencies.
 */

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher
