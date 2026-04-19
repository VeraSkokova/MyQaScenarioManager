package features.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import core.components.StatusBadge
import core.components.badgeColors
import features.dashboard.components.MetricCard

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {},
    onRunClick: (String) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    DashboardContent(
        state = state,
        isDarkTheme = isDarkTheme,
        onToggleTheme = onToggleTheme,
        onRunClick = onRunClick,
    )
}

@Composable
private fun DashboardContent(
    state: DashboardState,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onRunClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Dashboard",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.testTag("dashboard_title"),
            )
            IconButton(
                onClick = onToggleTheme,
                modifier = Modifier.testTag("theme_toggle_button"),
            ) {
                Icon(
                    imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = if (isDarkTheme) "Switch to light theme" else "Switch to dark theme",
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            MetricCard(
                title = "Pass Rate",
                value = "%.1f%%".format(state.passRatePercent),
                valueColor = if (state.passRatePercent >= 80f) Color(0xFF2E7D32) else Color(0xFFE65100),
                modifier = Modifier.weight(1f),
            )
            MetricCard(
                title = "Failed Today",
                value = state.failedTodayCount.toString(),
                valueColor = if (state.failedTodayCount > 0) Color(0xFFC62828) else Color(0xFF2E7D32),
                modifier = Modifier.weight(1f),
            )
            MetricCard(
                title = "Smoke Scenarios",
                value = "${state.smokeScenariosTotal} / ${state.totalScenarios}",
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(Modifier.height(32.dp))

        Text(
            text = "Recent Runs",
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.recentRuns, key = { it.id }) { run ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onRunClick(run.id) }
                        .testTag("recent_run_${run.id}"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = run.name,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "${run.environmentName} · ${run.scenarioCount} scenarios · ${run.createdAt}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        StatusBadge(
                            text = run.status.name.replace("_", " "),
                            colors = run.status.badgeColors(),
                        )
                    }
                }
            }
        }
    }
}
