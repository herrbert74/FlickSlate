package com.zsoltbertalan.flickslate.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zsoltbertalan.flickslate.R
import com.zsoltbertalan.flickslate.design.Colors
import com.zsoltbertalan.flickslate.design.LocalAppColors
import com.zsoltbertalan.flickslate.design.LocalAdditionalColorsPalette
import com.zsoltbertalan.flickslate.domain.model.Genre
import com.zsoltbertalan.flickslate.ui.component.ListTitle

@Composable
fun SearchScreen(
	modifier: Modifier = Modifier,
	searchState: SearchState,
	searchEvent: (SearchEvent) -> Unit,
	popTo: (Int, String) -> Unit,
) {
	Column(
		modifier = modifier
			.fillMaxSize()
			.background(Colors.background)
	) {
		SearchField(uiState = searchState, searchEvent = searchEvent)
		SearchResultUi(uiState = searchState)

		lightColorScheme()
		if (searchState.genreResult.isNotEmpty()) {
			ListTitle(titleId = R.string.genre)
			GenreList(
				list = searchState.genreResult,
				popTo = popTo
			)
		}
	}
}

@Composable
private fun SearchField(
	modifier: Modifier = Modifier,
	uiState: SearchState,
	searchEvent: (SearchEvent) -> Unit
) {
	TextField(
		value = uiState.searchTextField,
		onValueChange = {
			searchEvent(SearchEvent.SearchQuery(it))
		},
		modifier = modifier
			.padding(start = 8.dp, end = 8.dp, top = 8.dp)
			.fillMaxWidth(),
		trailingIcon = {
			if (uiState.searchResult.isNotEmpty()) {
				Icon(
					Icons.Rounded.Clear,
					contentDescription = "",
					modifier = Modifier.clickable {
						searchEvent(SearchEvent.SearchClear)
					}
				)
			} else {
				Icon(
					Icons.Rounded.Search,
					contentDescription = ""
				)
			}
		},
		placeholder = {
			Text(text = "look for movies here", color = Colors.surfaceDim)
		},
		colors = TextFieldDefaults.textFieldColors(
			containerColor = Color.White,
			cursorColor = Colors.primary,
			unfocusedIndicatorColor = Color.Transparent,
			focusedIndicatorColor = Color.Transparent,
			disabledIndicatorColor = Color.Transparent,
			unfocusedTrailingIconColor = Colors.background,
			focusedTrailingIconColor = Colors.primary,
		),
		shape = if (uiState.searchResult.isNotEmpty()) {
			RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp)
		} else {
			RoundedCornerShape(8.dp)
		}
	)
}

@Composable
private fun SearchResultUi(
	modifier: Modifier = Modifier,
	uiState: SearchState,
) {
	val isResultReady by remember(
		uiState.searchResult
	) { mutableStateOf(uiState.searchResult.isNotEmpty()) }
	AnimatedVisibility(visible = isResultReady,
		enter = slideInVertically(
			initialOffsetY = { -40 }
		) + expandVertically(
			expandFrom = Alignment.Top
		) + scaleIn(
			transformOrigin = TransformOrigin(0.5f, 0f)
		) + fadeIn(initialAlpha = 0.3f),
		exit = slideOutVertically() + shrinkVertically() + scaleOut()
	) {
		LazyColumn(
			modifier = modifier
				.height(200.dp)
				.fillMaxWidth()
				.padding(horizontal = 8.dp)
				.background(
					color = Color.White,
					shape = RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp)
				)
		) {
			itemsIndexed(uiState.searchResult, key = { index, item -> index }) { index, item ->
				Column(modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp)) {
					Text(text = item)
					HorizontalDivider(color = Colors.background)
				}
			}
		}
	}
}

@Composable
private fun GenreList(list: List<Genre>, popTo: (Int, String) -> Unit) {

	val listOfColors: List<Color> = listOf(
		LocalAppColors.current.primaryContainer,
		LocalAppColors.current.secondaryContainer,
		LocalAppColors.current.tertiaryContainer,
		LocalAdditionalColorsPalette.current.quaternaryContainer,
		LocalAdditionalColorsPalette.current.quinaryContainer,
		LocalAdditionalColorsPalette.current.senaryContainer,
	)
	val listRem by rememberSaveable {
		mutableStateOf(list)
	}
	LazyVerticalGrid(
		columns = GridCells.Fixed(2),
		contentPadding = PaddingValues(8.dp),
	) {
		itemsIndexed(items = listRem, key = { index, item ->
			item
		}) { index, item ->
			item.name?.let {
				Box(
					modifier = Modifier
						.clickable {
							item.id?.let {
								popTo(it, item.name)
							}
						}
						.padding(4.dp)
						.background(
							color = listOfColors[index % listOfColors.size],
							shape = RoundedCornerShape(8.dp)
						)
						.size(100.dp),
					contentAlignment = Alignment.Center
				) {
					Text(text = it, fontWeight = FontWeight.Bold, color = LocalAppColors.current.onPrimaryContainer)
				}
			}
		}
	}
}
