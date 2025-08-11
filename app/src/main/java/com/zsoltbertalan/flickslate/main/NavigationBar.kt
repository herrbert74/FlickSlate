package com.zsoltbertalan.flickslate.main

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zsoltbertalan.flickslate.R
import com.zsoltbertalan.flickslate.main.navigation.Destination
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.ui.compose.design.FlickSlateTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun FlickSlateBottomNavigationBar(
	navController: NavHostController,
	modifier: Modifier = Modifier,
	itemList: ImmutableList<Destination> =
		listOf(Destination.Movies, Destination.Tv, Destination.Search, Destination.Account).toImmutableList(),
) {
	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentRoute = navBackStackEntry?.destination?.route ?: Destination.Movies.javaClass.canonicalName

	FlickSlateNavigationBar(
		modifier = modifier,
	) {
		itemList.forEach { screen ->
			FlickSlateNavigationBarItem(
				selected = currentRoute == screen.javaClass.canonicalName,
				label = {
					Text(
						style = MaterialTheme.typography.bodySmall,
						text = stringResource(screen.toResourceId())
					)
				},
				onClick = {
					navController.navigate(screen) {
						popUpTo(navController.graph.findStartDestination().id) {
							saveState = true
						}
						launchSingleTop = true
						restoreState = true
					}
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
				painter = painterResource(id = R.drawable.ic_stars),
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
		FlickSlateBottomNavigationBar(
			navController = rememberNavController(),
		)
	}
}
