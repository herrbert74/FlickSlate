package com.zsoltbertalan.flickslate.tv.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zsoltbertalan.flickslate.shared.compose.component.paging.PaginationState
import com.zsoltbertalan.flickslate.shared.model.Failure
import com.zsoltbertalan.flickslate.tv.domain.api.TvRepository
import com.zsoltbertalan.flickslate.tv.domain.model.TvShow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvViewModel @Inject constructor(private val tvRepository: TvRepository) : ViewModel() {

	val tvPaginationState = PaginationState<Int, TvShow>(
		initialPageKey = 1,
		onRequestPage = {
			loadTvPage(it)
		}
	)

	private fun loadTvPage(pageKey: Int) {
		viewModelScope.launch {
			tvRepository.getTopRatedTv(page = pageKey).collect {
				when {
					it.isOk -> tvPaginationState.appendPage(
						items = it.value.pagingList,
						nextPageKey = if (it.value.isLastPage) -1 else pageKey + 1,
						isLastPage = it.value.isLastPage
					)

					else -> {
						val e = it.error as? Failure.ServerError
						tvPaginationState.setError(Exception(e?.message))
					}

				}
			}
		}
	}

}
