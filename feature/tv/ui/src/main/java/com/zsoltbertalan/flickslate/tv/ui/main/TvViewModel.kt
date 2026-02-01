package com.zsoltbertalan.flickslate.tv.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
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
				it.onSuccess { pagingReply ->
					tvPaginationState.appendPage(
						pageKey = pageKey,
						items = pagingReply.pagingList,
						nextPageKey = if (pagingReply.isLastPage) -1 else pageKey + 1,
						isLastPage = pagingReply.isLastPage
					)
				}.onFailure { failure ->
					tvPaginationState.setError(Exception(failure.message))
				}
			}
		}
	}

}
