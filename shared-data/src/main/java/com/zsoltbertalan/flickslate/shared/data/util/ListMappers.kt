package com.zsoltbertalan.flickslate.shared.data.util

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * Adapted for ImmutableLists from [com.babestudios.base.data.mapList] and others.
 */

// Non-nullable to Non-nullable
inline fun <I, O> mapImmutableList(input: List<I>, mapSingle: (I) -> O): ImmutableList<O> {
	return input.map { mapSingle(it) }.toImmutableList()
}

// Nullable to Non-nullable
inline fun <I, O> mapImmutableNullInputList(input: List<I>?, mapSingle: (I) -> O): ImmutableList<O> {
	return input?.map { mapSingle(it) }?.toImmutableList() ?: listOf<O>().toImmutableList()
}

// Non-nullable to Nullable
inline fun <I, O> mapImmutableNullOutputList(input: List<I>, mapSingle: (I) -> O): ImmutableList<O>? {
	return if (input.isEmpty()) null else input.map { mapSingle(it) }.toImmutableList()
}
