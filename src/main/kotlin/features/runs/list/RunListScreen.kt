package features.runs.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import core.components.StatusBadge
import core.components.badgeColors

@Composable
fun RunListScreen(
    viewModel: RunListViewModel,
    onRunClick: (String) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    RunListContent(
        state = state,
        onRunClick = onRunClick,
    )
}

@Composable
private fun RunListContent(
    state: RunListState,
    onRunClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        Text(
            text = "Test Runs",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.testTag("run_list_title"),
        )

        Spacer(Modifier.height(16.dp))

        if (state.runs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("run_list_empty"),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "No test runs yet",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.testTag("run_list"),
            ) {
                items(state.runs, key = { it.id }) { run ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onRunClick(run.id) }
                            .testTag("run_row_${run.id}"),
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
}
