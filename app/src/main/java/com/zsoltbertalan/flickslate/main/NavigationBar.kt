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
import com.zsoltbertalan.flickslate.shared.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.compose.design.FlickSlateTheme
import com.zsoltbertalan.flickslate.main.navigation.Destination

@Composable
fun FlickSlateBottomNavigationBar(
	navController: NavHostController,
	modifier: Modifier = Modifier,
	itemList: List<Destination> = Destination.entries,
) {

	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentRoute = navBackStackEntry?.destination?.route

	FlickSlateNavigationBar(
		modifier = modifier,
	) {
		itemList.forEach { category ->
			FlickSlateNavigationBarItem(
				selected = currentRoute == category.route,
				label = {
					Text(
						style = MaterialTheme.typography.bodySmall,
						text = stringResource(category.titleId)
					)
				},
				onClick = {
					navController.navigate(category.route) {
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
private fun FlickSlateBottomNavigationBarPreview() {
	FlickSlateTheme {
		FlickSlateBottomNavigationBar(
			navController = rememberNavController(),
		)
	}
}
