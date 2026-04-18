package features.scenarios.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import core.components.StatusBadge
import core.components.badgeColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.nio.file.Path

@Composable
fun ScenarioListScreen(
    viewModel: ScenarioListViewModel,
    onScenarioClick: (String) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    ScenarioListContent(
        state = state,
        onEvent = { event ->
            when (event) {
                is ScenarioListEvent.ScenarioSelected -> onScenarioClick(event.scenarioId)
                else -> viewModel.onEvent(event)
            }
        },
    )
}

@Composable
private fun ScenarioListContent(
    state: ScenarioListState,
    onEvent: (ScenarioListEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()

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
                text = "Scenarios",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.testTag("scenario_list_title"),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = {
                        scope.launch(Dispatchers.IO) {
                            chooseExportFile()?.let { path ->
                                onEvent(ScenarioListEvent.ExportScenarios(path))
                            }
                        }
                    },
                    modifier = Modifier.testTag("export_scenarios_button"),
                ) {
                    Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Export")
                }
                OutlinedButton(
                    onClick = {
                        scope.launch(Dispatchers.IO) {
                            chooseImportFile()?.let { path ->
                                onEvent(ScenarioListEvent.ImportScenarios(path))
                            }
                        }
                    },
                    modifier = Modifier.testTag("import_scenarios_button"),
                ) {
                    Icon(Icons.Default.FileUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Import")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        state.userMessage?.let { message ->
            Snackbar(
                action = {
                    TextButton(onClick = { onEvent(ScenarioListEvent.DismissMessage) }) {
                        Text("Dismiss")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("scenario_user_message"),
            ) {
                Text(message)
            }
            Spacer(Modifier.height(8.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { onEvent(ScenarioListEvent.SearchChanged(it)) },
                label = { Text("Search scenarios") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("scenario_search_field"),
            )
            FilterChip(
                selected = state.smokeOnly,
                onClick = { onEvent(ScenarioListEvent.SmokeFilterChanged(!state.smokeOnly)) },
                label = { Text("Smoke only") },
                modifier = Modifier.testTag("scenario_smoke_filter"),
            )
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.testTag("scenario_list"),
        ) {
            items(state.scenarios, key = { it.id }) { item ->
                ScenarioRow(
                    item = item,
                    onClick = { onEvent(ScenarioListEvent.ScenarioSelected(item.id)) },
                )
            }
        }
    }
}

private fun chooseExportFile(): Path? {
    val dialog = FileDialog(null as Frame?, "Export Scenarios", FileDialog.SAVE)
    dialog.file = "scenarios.json"
    dialog.isVisible = true
    val dir = dialog.directory ?: return null
    val file = dialog.file ?: return null
    val target = if (file.endsWith(".json")) file else "$file.json"
    return File(dir, target).toPath()
}

private fun chooseImportFile(): Path? {
    val dialog = FileDialog(null as Frame?, "Import Scenarios", FileDialog.LOAD)
    dialog.setFilenameFilter { _, name -> name.endsWith(".json") }
    dialog.isVisible = true
    val dir = dialog.directory ?: return null
    val file = dialog.file ?: return null
    return File(dir, file).toPath()
}

@Composable
private fun ScenarioRow(
    item: ScenarioListItemUi,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("scenario_row_${item.id}"),
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
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatusBadge(
                        text = item.type.name,
                        colors = if (item.type == domain.model.ScenarioType.SMOKE) {
                            core.components.BadgeColors(
                                androidx.compose.ui.graphics.Color(0xFF00897B),
                                androidx.compose.ui.graphics.Color(0xFFE0F2F1),
                            )
                        } else {
                            core.components.BadgeColors(
                                androidx.compose.ui.graphics.Color(0xFF5E35B1),
                                androidx.compose.ui.graphics.Color(0xFFEDE7F6),
                            )
                        },
                    )
                    StatusBadge(
                        text = item.priority.name,
                        colors = item.priority.badgeColors(),
                    )
                }
                if (item.tagNames.isNotEmpty()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = item.tagNames.joinToString(", "),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Text(
                text = "${item.stepCount} steps",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
