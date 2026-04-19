package features.scenarios.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import core.components.StatusBadge
import core.components.badgeColors

@Composable
fun ScenarioDetailsScreen(
    viewModel: ScenarioDetailsViewModel,
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    ScenarioDetailsContent(
        state = state,
        onBack = onBack,
    )
}

@Composable
private fun ScenarioDetailsContent(
    state: ScenarioDetailsState,
    onBack: () -> Unit,
) {
    val scenario = state.scenario

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("scenario_details_back"),
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = scenario?.title ?: "Scenario not found",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.testTag("scenario_details_title"),
            )
        }

        if (scenario == null) return

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                Text(
                    text = scenario.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatusBadge(
                        text = scenario.type.name,
                        colors = scenario.type.badgeColors(),
                    )
                    StatusBadge(
                        text = scenario.priority.name,
                        colors = scenario.priority.badgeColors(),
                    )
                    scenario.tagNames.forEach { tag ->
                        StatusBadge(
                            text = tag,
                            colors = core.components.BadgeColors(
                                text = MaterialTheme.colorScheme.onSurfaceVariant,
                                background = MaterialTheme.colorScheme.surfaceVariant,
                            ),
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Steps",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }

            itemsIndexed(scenario.steps) { index, step ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("scenario_step_${index + 1}"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Step ${step.orderIndex}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = step.action,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Expected: ${step.expectedResult}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            item {
                Column {
                    Text(
                        text = "Expected Result",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = scenario.expectedResult,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            if (state.linkedDefects.isNotEmpty()) {
                item {
                    Text(
                        text = "Linked Defects",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
                state.linkedDefects.forEach { defect ->
                    item(key = defect.id) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("linked_defect_${defect.id}"),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = defect.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = "${defect.status.name.replace("_", " ")} · ${defect.url}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
