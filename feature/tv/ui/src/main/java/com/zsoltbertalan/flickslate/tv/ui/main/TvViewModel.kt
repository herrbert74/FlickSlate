package com.zsoltbertalan.flickslate.tv.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import com.zsoltbertalan.flickslate.shared.ui.compose.component.paging.PaginationState
import com.zsoltbertalan.flickslate.tv.domain.api.TvRepository
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

					else -> tvPaginationState.setError(Exception(it.error.message))

				}
			}
		}
	}

}
