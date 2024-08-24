package com.zsoltbertalan.flickslate.presentation.ui.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zsoltbertalan.flickslate.domain.api.TvRepository
import com.zsoltbertalan.flickslate.domain.model.Failure
import com.zsoltbertalan.flickslate.domain.model.Tv
import com.zsoltbertalan.flickslate.presentation.component.paging.PaginationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvViewModel @Inject constructor(private val tvRepository: TvRepository) : ViewModel() {

	val tvPaginationState = PaginationState<Int, Tv>(
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
