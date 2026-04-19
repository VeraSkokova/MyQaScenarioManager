package features.scenarios.list

import data.InMemoryScenarioRepository
import domain.model.Scenario
import domain.model.ScenarioPriority
import domain.model.ScenarioType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ScenarioListViewModelTest {

    private val scenarios = listOf(
        scenario("1", "Login Flow", "User logs in", ScenarioType.SMOKE),
        scenario("2", "Checkout Process", "Full checkout with payment", ScenarioType.FUNCTIONAL),
        scenario("3", "Dashboard Smoke", "Verify dashboard loads", ScenarioType.SMOKE),
    )

    @Test
    fun `empty query returns all scenarios`() {
        val vm = createViewModel()
        vm.onEvent(ScenarioListEvent.SearchChanged(""))
        assertEquals(3, vm.state.value.scenarios.size)
    }

    @Test
    fun `partial match filters by title`() {
        val vm = createViewModel()
        vm.onEvent(ScenarioListEvent.SearchChanged("login"))
        assertEquals(1, vm.state.value.scenarios.size)
        assertEquals("1", vm.state.value.scenarios[0].id)
    }

    @Test
    fun `partial match filters by description`() {
        val vm = createViewModel()
        vm.onEvent(ScenarioListEvent.SearchChanged("payment"))
        assertEquals(1, vm.state.value.scenarios.size)
        assertEquals("2", vm.state.value.scenarios[0].id)
    }

    @Test
    fun `search is case insensitive`() {
        val vm = createViewModel()
        vm.onEvent(ScenarioListEvent.SearchChanged("LOGIN"))
        assertEquals(1, vm.state.value.scenarios.size)
    }

    @Test
    fun `no match returns empty list`() {
        val vm = createViewModel()
        vm.onEvent(ScenarioListEvent.SearchChanged("nonexistent"))
        assertEquals(0, vm.state.value.scenarios.size)
    }

    @Test
    fun `blank query returns all scenarios`() {
        val vm = createViewModel()
        vm.onEvent(ScenarioListEvent.SearchChanged("   "))
        assertEquals(3, vm.state.value.scenarios.size)
    }

    // --- Smoke-only filter tests ---

    @Test
    fun `smoke filter off returns all scenarios`() {
        val vm = createViewModel()
        vm.onEvent(ScenarioListEvent.SmokeFilterChanged(false))
        assertEquals(3, vm.state.value.scenarios.size)
    }

    @Test
    fun `smoke filter on returns only smoke scenarios`() {
        val vm = createViewModel()
        vm.onEvent(ScenarioListEvent.SmokeFilterChanged(true))
        assertEquals(2, vm.state.value.scenarios.size)
        vm.state.value.scenarios.forEach {
            assertEquals(ScenarioType.SMOKE, it.type)
        }
    }

    @Test
    fun `smoke filter combined with search`() {
        val vm = createViewModel()
        vm.onEvent(ScenarioListEvent.SmokeFilterChanged(true))
        vm.onEvent(ScenarioListEvent.SearchChanged("dashboard"))
        assertEquals(1, vm.state.value.scenarios.size)
        assertEquals("3", vm.state.value.scenarios[0].id)
    }

    @Test
    fun `smoke filter with non-smoke search returns empty`() {
        val vm = createViewModel()
        vm.onEvent(ScenarioListEvent.SmokeFilterChanged(true))
        vm.onEvent(ScenarioListEvent.SearchChanged("checkout"))
        assertEquals(0, vm.state.value.scenarios.size)
    }

    private fun createViewModel(): ScenarioListViewModel {
        return ScenarioListViewModel(InMemoryScenarioRepository(scenarios))
    }

    private fun scenario(
        id: String,
        title: String,
        description: String,
        type: ScenarioType,
    ) = Scenario(
        id = id,
        title = title,
        description = description,
        type = type,
        priority = ScenarioPriority.MEDIUM,
        tags = emptyList(),
        steps = emptyList(),
        expectedResult = "Success",
    )
}
