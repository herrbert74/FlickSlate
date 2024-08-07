package com.zsoltbertalan.flickslate.presentation.ui.tv

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.zsoltbertalan.flickslate.domain.model.Tv
import com.zsoltbertalan.flickslate.R
import com.zsoltbertalan.flickslate.presentation.design.Colors
import com.zsoltbertalan.flickslate.domain.model.MovieCardType
import com.zsoltbertalan.flickslate.presentation.util.navigate
import com.zsoltbertalan.flickslate.presentation.component.ListTitle
import com.zsoltbertalan.flickslate.presentation.component.ShowLoading
import com.zsoltbertalan.flickslate.presentation.component.ShowCard

@Composable
fun TvScreen(
	modifier: Modifier = Modifier,
	topRatedTv: LazyPagingItems<Tv>,
	popTo: (Int) -> Unit,
) {
	LazyColumn(
		modifier = modifier
			.background(Colors.surface)
			.fillMaxHeight()
	) {
		showTopRatedTv(topRatedTv, popTo)
	}
}

private fun LazyListScope.showTopRatedTv(
	topRatedTv: LazyPagingItems<Tv>,
	popTo: (Int) -> Unit,
) {

	item {
		ListTitle(titleId = R.string.top_rated_tv)
	}

	items(topRatedTv.itemCount) { index ->
		topRatedTv[index].let {
			it?.let {
				ShowCard(
					modifier = Modifier.navigate(it.id, popTo),
					title = it.name,
					voteAverage = it.voteAverage,
					overview = it.overview,
					posterPath = it.posterPath,
					cardType = MovieCardType.FULL
				)
			}

		}
	}

	item {
		Spacer(modifier = Modifier.height(20.dp))
	}

	when {
		topRatedTv.loadState.refresh is LoadState.Loading -> item {
			ShowLoading(
				text = stringResource(id = R.string.top_rated_tv)
			)
		}

		topRatedTv.loadState.append is LoadState.Loading -> item {
			ShowLoading(
				text = stringResource(id = R.string.top_rated_tv)
			)
		}

	}

}
