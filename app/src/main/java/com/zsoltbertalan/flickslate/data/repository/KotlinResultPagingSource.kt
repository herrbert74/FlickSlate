package com.zsoltbertalan.flickslate.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zsoltbertalan.flickslate.common.util.Outcome
import com.zsoltbertalan.flickslate.domain.model.Failure

const val PAGING_PAGE_SIZE = 30
const val PAGING_PREFETCH_DISTANCE = 5

class PagingException(message: String) : Exception(message)

fun <V : Any> createPager(
	pageSize: Int = PAGING_PAGE_SIZE,
	block: suspend (Int) -> Outcome<Pair<List<V>, Int>>
): Pager<Int, V> = Pager(
	config = PagingConfig(pageSize = pageSize, prefetchDistance = PAGING_PREFETCH_DISTANCE),
	pagingSourceFactory = { KotlinResultPagingSource(block) }
)

class KotlinResultPagingSource<T : Any>(
	private val block: suspend (Int) -> Outcome<Pair<List<T>, Int>>
) : PagingSource<Int, T>() {

	override fun getRefreshKey(state: PagingState<Int, T>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
				?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
		}
	}

	/**
	 * Executes the loading block, with which this source was initialised.
	 *
	 * Because [androidx.paging.PagingSource.LoadResult] can only handle [Throwable]s,
	 * we have to wrap the [Failure] message into an Exception and rethrow it to handle them together with other Errors.
	 */
	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
		val page = params.key ?: 1
		return try {
			val result = block(page)
			when {
				result.isOk -> LoadResult.Page(
					data = result.value.first,
					prevKey = if (page == 1) null else page - 1,
					nextKey = if (page == result.value.second) null else page + 1
				)

				else -> {
					val serverError = result.error as Failure.ServerError
					throw PagingException(serverError.message)
				}
			}
		} catch (e: PagingException) {
			LoadResult.Error(e)
		}
	}

}
