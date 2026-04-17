package features.runs.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import core.components.StatusBadge
import core.components.badgeColors
import domain.model.ResultStatus

@Composable
fun RunDetailsScreen(
    viewModel: RunDetailsViewModel,
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    RunDetailsContent(
        state = state,
        onEvent = { event ->
            when (event) {
                is RunDetailsEvent.BackClicked -> onBack()
                else -> viewModel.onEvent(event)
            }
        },
    )
}

@Composable
private fun RunDetailsContent(
    state: RunDetailsState,
    onEvent: (RunDetailsEvent) -> Unit,
) {
    val run = state.run

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { onEvent(RunDetailsEvent.BackClicked) },
                modifier = Modifier.testTag("run_details_back"),
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = run?.name ?: "Run not found",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.testTag("run_details_title"),
            )
        }

        if (run == null) return

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    InfoItem("Environment", run.environmentName)
                    InfoItem("Build", "${run.buildVersion} (${run.buildBranch})")
                    InfoItem("Created", run.createdAt)
                    run.completedAt?.let { InfoItem("Completed", it) }
                }
                Spacer(Modifier.height(8.dp))
                StatusBadge(
                    text = run.status.name.replace("_", " "),
                    colors = run.status.badgeColors(),
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Scenario Results",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.testTag("run_results_list"),
        ) {
            items(state.results, key = { it.resultId }) { result ->
                ResultRow(
                    result = result,
                    onStatusChange = { newStatus ->
                        onEvent(RunDetailsEvent.StatusChanged(result.resultId, newStatus))
                    },
                )
            }
        }
    }
}

@Composable
private fun InfoItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun ResultRow(
    result: RunResultRowUi,
    onStatusChange: (ResultStatus) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val statusOptions = listOf(ResultStatus.PASSED, ResultStatus.FAILED, ResultStatus.BLOCKED, ResultStatus.NOT_RUN)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("result_row_${result.resultId}"),
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
                    text = result.scenarioTitle,
                    style = MaterialTheme.typography.bodyLarge,
                )
                if (result.comment.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = result.comment,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (result.executedAt != null) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "Executed: ${result.executedAt}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Box {
                TextButton(
                    onClick = { expanded = true },
                    modifier = Modifier.testTag("result_status_${result.resultId}"),
                ) {
                    StatusBadge(
                        text = result.status.name.replace("_", " "),
                        colors = result.status.badgeColors(),
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    statusOptions.forEach { status ->
                        DropdownMenuItem(
                            text = {
                                StatusBadge(
                                    text = status.name.replace("_", " "),
                                    colors = status.badgeColors(),
                                )
                            },
                            onClick = {
                                expanded = false
                                onStatusChange(status)
                            },
                            modifier = Modifier.testTag("result_status_option_${status.name.lowercase()}"),
                        )
                    }
                }
            }
        }
    }
}
