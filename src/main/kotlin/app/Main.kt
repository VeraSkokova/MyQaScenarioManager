package app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import core.theme.AppTheme

fun main(): Unit = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "QA Scenario Manager",
        state = rememberWindowState(width = 1200.dp, height = 800.dp),
    ) {
        var isDarkTheme by remember { mutableStateOf(false) }
        AppTheme(isDarkTheme = isDarkTheme) {
            val navigationState = remember { NavigationState() }
            AppContent(
                navigationState = navigationState,
                isDarkTheme = isDarkTheme,
                onToggleTheme = { isDarkTheme = !isDarkTheme },
            )
        }
    }
}
