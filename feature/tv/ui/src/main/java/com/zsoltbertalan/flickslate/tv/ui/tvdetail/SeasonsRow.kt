package com.zsoltbertalan.flickslate.tv.ui.tvdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zsoltbertalan.flickslate.shared.ui.compose.component.FilledButton
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Dimens
import com.zsoltbertalan.flickslate.shared.ui.compose.design.FlickSlateTheme
import com.zsoltbertalan.flickslate.tv.domain.model.Season
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun SeasonsRow(seasons: ImmutableList<Season>, modifier: Modifier = Modifier) {
	LazyRow(
		modifier
			.fillMaxWidth(1f)
			.wrapContentHeight(align = Alignment.Top),
		horizontalArrangement = Arrangement.Start,
	) {
		items(seasons.size) { index ->
			seasons[index].name?.let {
				FilledButton(
					modifier = Modifier
						.padding(end = Dimens.marginSmall),
					onClick = { },
					content = {
						Text(
							modifier = Modifier.padding(Dimens.marginSmall),
							text = it
						)
					}
				)
			}
		}
	}
}

@Preview
@Composable
internal fun SeasonChipsPreview() {
	fun getSeason(name: String) = Season(
		airDate = "2023-2-2",
		episodeCount = 13,
		id = 1232,
		name = name,
		overview = "Overview",
		posterPath = "",
		seasonNumber = 1,
	)

	FlickSlateTheme {
		SeasonsRow(
			modifier = Modifier,
			seasons = listOf(
				getSeason("Season 1"),
				getSeason("Season 2"),
				getSeason("Season 3"),
				getSeason("Season 4"),
			).toImmutableList()
		)
	}

}
