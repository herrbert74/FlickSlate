package com.zsoltbertalan.flickslate.presentation.ui.search

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zsoltbertalan.flickslate.R
import com.zsoltbertalan.flickslate.presentation.design.AdditionalColors
import com.zsoltbertalan.flickslate.presentation.design.Colors
import com.zsoltbertalan.flickslate.domain.model.Genre
import com.zsoltbertalan.flickslate.presentation.component.ListTitle

@Composable
fun SearchScreen(
	modifier: Modifier = Modifier,
	searchState: SearchState,
	searchEvent: (SearchEvent) -> Unit,
	navigateToGenreDetails: (Int, String) -> Unit,
	navigateToMovieDetails: (Int) -> Unit,
) {

	var isSearchBarActive by rememberSaveable { mutableStateOf(false) }
	var searchQuery by rememberSaveable { mutableStateOf("") }
	val focusManager = LocalFocusManager.current

	fun closeSearchBar() {
		focusManager.clearFocus()
		isSearchBarActive = false
	}

	Column(
		modifier = modifier
			.fillMaxSize()
			.background(Colors.surface)
	) {
		ShowSearchBar(
			searchQuery,
			{
				searchQuery = it
				searchEvent(SearchEvent.SearchQuery(it))
			},
			{ closeSearchBar() },
			{
				isSearchBarActive = it
				if (!isSearchBarActive) focusManager.clearFocus()
			},
			{
				isSearchBarActive = false
				searchQuery = ""
				searchEvent(SearchEvent.SearchQuery(""))
			},
			searchState,
			navigateToGenreDetails,
			navigateToMovieDetails
		)
	}
}

@Composable
private fun ShowSearchBar(
	searchQuery: String,
	onQueryChange: (String) -> Unit,
	onSearch: () -> Unit,
	onActiveChange: (Boolean) -> Unit,
	onQueryClose: () -> Unit,
	searchState: SearchState,
	navigateToGenreDetails: (Int, String) -> Unit,
	navigateToMovieDetails: (Int) -> Unit
) {
	SearchBar(
		query = searchQuery,
		onQueryChange = onQueryChange,
		onSearch = { onSearch() },
		active = true,
		onActiveChange = {
			onActiveChange(it)
		},
		placeholder = { Text(stringResource(R.string.search_prompt)) },
		leadingIcon = {
			Icon(
				modifier = Modifier.clickable {
					onQueryClose()
				},
				imageVector = Icons.AutoMirrored.Filled.ArrowBack,
				contentDescription = null
			)
		}) {
		SearchResultUi(uiState = searchState, navigateToMovieDetails = navigateToMovieDetails)
		if (searchState.genreResult.isNotEmpty()) {
			ListTitle(titleId = R.string.genre)
			GenreList(
				list = searchState.genreResult,
				navigateToGenreDetails = navigateToGenreDetails
			)
		}
	}

}

@Composable
private fun SearchResultUi(
	modifier: Modifier = Modifier,
	uiState: SearchState,
	navigateToMovieDetails: (Int) -> Unit
) {
	val isResultReady by remember(
		uiState.searchResult
	) { mutableStateOf(uiState.searchResult.isNotEmpty()) }
	AnimatedVisibility(
		visible = isResultReady,
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
					color = Colors.surfaceContainerLow,
					shape = RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp)
				)
		) {
			itemsIndexed(uiState.searchResult, key = { index, _ -> index }) { _, item ->
				Column(modifier = Modifier
					.padding(vertical = 4.dp, horizontal = 4.dp)
					.clickable { navigateToMovieDetails(item.id) }
				) {
					Text(text = item.title)
					HorizontalDivider(color = Colors.outlineVariant)
				}
			}
		}
	}
}

@Composable
private fun GenreList(list: List<Genre>, navigateToGenreDetails: (Int, String) -> Unit) {

	val listOfColors: List<Color> = listOf(
		Colors.primaryContainer,
		Colors.secondaryContainer,
		Colors.tertiaryContainer,
		AdditionalColors.quaternaryContainer,
		AdditionalColors.quinaryContainer,
		AdditionalColors.senaryContainer,
	)
	val listRem by rememberSaveable {
		mutableStateOf(list)
	}
	LazyVerticalGrid(
		columns = GridCells.Fixed(2),
		contentPadding = PaddingValues(8.dp),
	) {
		itemsIndexed(
			items = listRem,
			key = { _, item -> item }
		) { index, item ->
			item.name?.let {
				Box(
					modifier = Modifier
						.clickable {
							item.id?.let {
								navigateToGenreDetails(it, item.name)
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
					Text(text = it, fontWeight = FontWeight.Bold, color = Colors.onPrimaryContainer)
				}
			}
		}
	}
}
