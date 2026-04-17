package core.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import app.Route

data class NavItem(
    val route: Route,
    val label: String,
    val icon: @Composable () -> Unit,
    val testTag: String,
)

@Composable
fun SidebarNavigation(
    currentRoute: Route,
    onNavigate: (Route) -> Unit,
    modifier: Modifier = Modifier,
) {
    val items = listOf(
        NavItem(Route.Dashboard, "Dashboard", { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") }, "nav_dashboard"),
        NavItem(Route.ScenarioList, "Scenarios", { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Scenarios") }, "nav_scenarios"),
        NavItem(Route.RunList, "Runs", { Icon(Icons.Default.PlayArrow, contentDescription = "Runs") }, "nav_runs"),
        NavItem(Route.Settings, "Settings", { Icon(Icons.Default.Settings, contentDescription = "Settings") }, "nav_settings"),
    )

    NavigationRail(modifier = modifier.padding(top = 12.dp)) {
        items.forEach { item ->
            val selected = when {
                item.route is Route.Dashboard && currentRoute is Route.Dashboard -> true
                item.route is Route.ScenarioList && (currentRoute is Route.ScenarioList || currentRoute is Route.ScenarioDetails) -> true
                item.route is Route.RunList && (currentRoute is Route.RunList || currentRoute is Route.RunDetails) -> true
                item.route is Route.Settings && currentRoute is Route.Settings -> true
                else -> false
            }
            NavigationRailItem(
                selected = selected,
                onClick = { onNavigate(item.route) },
                icon = item.icon,
                label = { Text(item.label) },
                modifier = Modifier.testTag(item.testTag),
            )
        }
    }
}
