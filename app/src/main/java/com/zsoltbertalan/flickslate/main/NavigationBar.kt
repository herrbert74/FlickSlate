package com.zsoltbertalan.flickslate.main

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zsoltbertalan.flickslate.R
import com.zsoltbertalan.flickslate.main.navigation.Destination
import com.zsoltbertalan.flickslate.main.navigation.NavigationState
import com.zsoltbertalan.flickslate.main.navigation.Navigator
import com.zsoltbertalan.flickslate.main.navigation.rememberNavigationState
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.ui.compose.design.FlickSlateTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun FlickSlateBottomNavigationBar(
	navigationState: NavigationState,
	navigator: Navigator,
	modifier: Modifier = Modifier,
	itemList: ImmutableList<Destination> =
		listOf(Destination.Movies, Destination.Tv, Destination.Search, Destination.Account).toImmutableList(),
) {
	val currentRoute = navigationState.topLevelRoute

	FlickSlateNavigationBar(
		modifier = modifier,
	) {
		itemList.forEach { screen ->
			FlickSlateNavigationBarItem(
				selected = currentRoute == screen,
				iconResId = screen.toIconResourceId(),
				label = {
					Text(
						style = MaterialTheme.typography.bodySmall,
						text = stringResource(screen.toResourceId())
					)
				},
				onClick = {
					navigator.navigate(screen)
				},
			)
		}
	}
}

private fun Destination.toResourceId(): Int = when (this) {
	is Destination.Movies -> R.string.movies
	is Destination.Tv -> R.string.tv
	is Destination.Search -> R.string.search
	is Destination.Account -> R.string.account
	else -> R.string.movies
}

@DrawableRes
private fun Destination.toIconResourceId(): Int = when (this) {
	is Destination.Movies -> R.drawable.ic_movie
	is Destination.Tv -> R.drawable.ic_tv
	is Destination.Search -> R.drawable.ic_search
	is Destination.Account -> R.drawable.ic_account_box
	else -> R.drawable.ic_movie
}

@Composable
fun FlickSlateNavigationBar(
	modifier: Modifier = Modifier,
	content: @Composable RowScope.() -> Unit
) {
	NavigationBar(
		containerColor = Colors.surface,
		modifier = modifier,
		content = content
	)
}

@Composable
fun RowScope.FlickSlateNavigationBarItem(
	selected: Boolean,
	@DrawableRes iconResId: Int,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	label: @Composable (() -> Unit)? = null,
	alwaysShowLabel: Boolean = true,
	onClick: () -> Unit
) {
	NavigationBarItem(
		selected = selected,
		modifier = modifier,
		enabled = enabled,
		label = label,
		alwaysShowLabel = alwaysShowLabel,
		colors = NavigationBarItemDefaults.colors(
			selectedTextColor = Colors.onSurface,
			unselectedTextColor = Colors.onSurfaceVariant,
			unselectedIconColor = Colors.onSurfaceVariant
		),
		icon = {
			Icon(
				painter = painterResource(id = iconResId),
				contentDescription = "",
				modifier = Modifier.height(20.dp),
			)
		},
		onClick = onClick
	)
}

@Preview(showBackground = true)
@Composable
internal fun FlickSlateBottomNavigationBarPreview() {
	FlickSlateTheme {
		val navigationState = rememberNavigationState(
			startRoute = Destination.Movies,
			topLevelRoutes = persistentSetOf(
				Destination.Movies,
				Destination.Tv,
				Destination.Search,
				Destination.Account
			)
		)
		val navigator = remember { Navigator(navigationState) }
		FlickSlateBottomNavigationBar(
			navigationState = navigationState,
			navigator = navigator,
		)
	}
}
