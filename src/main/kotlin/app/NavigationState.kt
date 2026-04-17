package app

import androidx.compose.runtime.mutableStateOf

class NavigationState {
    val currentRoute = mutableStateOf<Route>(Route.Dashboard)

    private val backStack = mutableListOf<Route>()

    fun navigateTo(route: Route) {
        backStack.clear()
        currentRoute.value = route
    }

    fun navigateWithBackStack(route: Route) {
        backStack.add(currentRoute.value)
        currentRoute.value = route
    }

    fun goBack() {
        if (backStack.isNotEmpty()) {
            currentRoute.value = backStack.removeLast()
        }
    }
}
