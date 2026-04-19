package app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import core.components.SidebarNavigation
import features.dashboard.DashboardScreen
import features.runs.details.RunDetailsScreen
import features.runs.list.RunListScreen
import features.scenarios.details.ScenarioDetailsScreen
import features.scenarios.list.ScenarioListScreen

@Composable
fun AppContent(
    navigationState: NavigationState,
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {},
) {
    Row(modifier = Modifier.fillMaxSize()) {
        SidebarNavigation(
            currentRoute = navigationState.currentRoute.value,
            onNavigate = { route -> navigationState.navigateTo(route) },
        )
        VerticalDivider()
        Box(modifier = Modifier.weight(1f).fillMaxSize()) {
            when (val route = navigationState.currentRoute.value) {
                is Route.Dashboard -> {
                    val viewModel = remember { AppModule.dashboardViewModel() }
                    DashboardScreen(
                        viewModel = viewModel,
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = onToggleTheme,
                        onRunClick = { runId -> navigationState.navigateWithBackStack(Route.RunDetails(runId)) },
                    )
                }
                is Route.ScenarioList -> {
                    val viewModel = remember { AppModule.scenarioListViewModel() }
                    ScenarioListScreen(
                        viewModel = viewModel,
                        onScenarioClick = { id -> navigationState.navigateWithBackStack(Route.ScenarioDetails(id)) },
                    )
                }
                is Route.ScenarioDetails -> {
                    val viewModel = remember(route.scenarioId) { AppModule.scenarioDetailsViewModel(route.scenarioId) }
                    ScenarioDetailsScreen(
                        viewModel = viewModel,
                        onBack = { navigationState.goBack() },
                    )
                }
                is Route.RunList -> {
                    val viewModel = remember { AppModule.runListViewModel() }
                    RunListScreen(
                        viewModel = viewModel,
                        onRunClick = { id -> navigationState.navigateWithBackStack(Route.RunDetails(id)) },
                    )
                }
                is Route.RunDetails -> {
                    val viewModel = remember(route.runId) { AppModule.runDetailsViewModel(route.runId) }
                    RunDetailsScreen(
                        viewModel = viewModel,
                        onBack = { navigationState.goBack() },
                    )
                }
                is Route.Settings -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Settings — coming soon",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}
