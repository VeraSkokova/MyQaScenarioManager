package data

import domain.model.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

object SeedData {

    val tags: List<Tag> = listOf(
        Tag(id = "tag-auth", name = "auth"),
        Tag(id = "tag-payment", name = "payment"),
        Tag(id = "tag-ui", name = "ui"),
        Tag(id = "tag-api", name = "api"),
        Tag(id = "tag-regression", name = "regression"),
        Tag(id = "tag-smoke", name = "smoke"),
    )

    private val tagsByName: Map<String, Tag> = tags.associateBy { it.name }
    private fun tags(vararg names: String): List<Tag> = names.mapNotNull { tagsByName[it] }

    val scenarios: List<Scenario> = listOf(
        Scenario(
            id = "scenario-1",
            title = "Login with valid credentials",
            description = "Verify that a user can log in with a valid email and password.",
            type = ScenarioType.SMOKE,
            priority = ScenarioPriority.CRITICAL,
            tags = tags("auth", "smoke"),
            steps = listOf(
                ScenarioStep("step-1-1", 1, "Open the login page", "Login form is displayed"),
                ScenarioStep("step-1-2", 2, "Enter valid email and password", "Fields accept input"),
                ScenarioStep("step-1-3", 3, "Click the Sign In button", "User is redirected to the dashboard"),
            ),
            expectedResult = "User is authenticated and sees the dashboard.",
        ),
        Scenario(
            id = "scenario-2",
            title = "Login with invalid credentials",
            description = "Verify that login fails gracefully with wrong credentials.",
            type = ScenarioType.FUNCTIONAL,
            priority = ScenarioPriority.HIGH,
            tags = tags("auth", "regression"),
            steps = listOf(
                ScenarioStep("step-2-1", 1, "Open the login page", "Login form is displayed"),
                ScenarioStep("step-2-2", 2, "Enter invalid email or wrong password", "Fields accept input"),
                ScenarioStep("step-2-3", 3, "Click the Sign In button", "Error message is shown"),
            ),
            expectedResult = "Error message: 'Invalid email or password' is displayed.",
            linkedDefectIds = listOf("defect-1"),
        ),
        Scenario(
            id = "scenario-3",
            title = "User registration",
            description = "Verify that a new user can create an account.",
            type = ScenarioType.SMOKE,
            priority = ScenarioPriority.CRITICAL,
            tags = tags("auth", "smoke"),
            steps = listOf(
                ScenarioStep("step-3-1", 1, "Open the registration page", "Registration form is displayed"),
                ScenarioStep("step-3-2", 2, "Fill in name, email, and password", "Fields accept input"),
                ScenarioStep("step-3-3", 3, "Click Register button", "Confirmation message is shown"),
                ScenarioStep("step-3-4", 4, "Check email for verification link", "Verification email received"),
            ),
            expectedResult = "Account is created and verification email is sent.",
        ),
        Scenario(
            id = "scenario-4",
            title = "Forgot password flow",
            description = "Verify the password reset flow works end to end.",
            type = ScenarioType.FUNCTIONAL,
            priority = ScenarioPriority.MEDIUM,
            tags = tags("auth"),
            steps = listOf(
                ScenarioStep("step-4-1", 1, "Click 'Forgot password?' on login page", "Reset form is displayed"),
                ScenarioStep("step-4-2", 2, "Enter registered email", "Field accepts input"),
                ScenarioStep("step-4-3", 3, "Click Send Reset Link", "Success message is shown"),
                ScenarioStep("step-4-4", 4, "Open reset link from email", "New password form is displayed"),
                ScenarioStep("step-4-5", 5, "Enter and confirm new password", "Password is updated"),
            ),
            expectedResult = "User can log in with the new password.",
        ),
        Scenario(
            id = "scenario-5",
            title = "Add item to cart",
            description = "Verify that a user can add a product to the shopping cart.",
            type = ScenarioType.SMOKE,
            priority = ScenarioPriority.HIGH,
            tags = tags("ui", "smoke"),
            steps = listOf(
                ScenarioStep("step-5-1", 1, "Navigate to a product page", "Product details are displayed"),
                ScenarioStep("step-5-2", 2, "Click 'Add to Cart' button", "Item is added to the cart"),
                ScenarioStep("step-5-3", 3, "Open the cart page", "Cart shows the added item with correct quantity"),
            ),
            expectedResult = "Cart contains the selected product with quantity 1.",
        ),
        Scenario(
            id = "scenario-6",
            title = "Checkout flow",
            description = "Verify the full checkout process from cart to order confirmation.",
            type = ScenarioType.FUNCTIONAL,
            priority = ScenarioPriority.CRITICAL,
            tags = tags("payment", "regression"),
            steps = listOf(
                ScenarioStep("step-6-1", 1, "Add items to cart and go to checkout", "Checkout page is displayed"),
                ScenarioStep("step-6-2", 2, "Enter shipping address", "Address is accepted"),
                ScenarioStep("step-6-3", 3, "Select payment method and enter details", "Payment form is filled"),
                ScenarioStep("step-6-4", 4, "Click Place Order", "Order confirmation page is displayed"),
            ),
            expectedResult = "Order is placed and confirmation number is shown.",
            linkedDefectIds = listOf("defect-2"),
        ),
        Scenario(
            id = "scenario-7",
            title = "Search products",
            description = "Verify that the product search returns relevant results.",
            type = ScenarioType.SMOKE,
            priority = ScenarioPriority.MEDIUM,
            tags = tags("ui", "smoke"),
            steps = listOf(
                ScenarioStep("step-7-1", 1, "Click the search bar", "Search field is focused"),
                ScenarioStep("step-7-2", 2, "Type a product name", "Search suggestions appear"),
                ScenarioStep("step-7-3", 3, "Press Enter or click a suggestion", "Search results page shows matching products"),
            ),
            expectedResult = "Results page displays relevant products matching the query.",
            linkedDefectIds = listOf("defect-3"),
        ),
        Scenario(
            id = "scenario-8",
            title = "Admin dashboard access",
            description = "Verify that admin users can access the admin dashboard.",
            type = ScenarioType.FUNCTIONAL,
            priority = ScenarioPriority.HIGH,
            tags = tags("auth", "api"),
            steps = listOf(
                ScenarioStep("step-8-1", 1, "Log in with admin credentials", "Admin is authenticated"),
                ScenarioStep("step-8-2", 2, "Navigate to /admin/dashboard", "Admin dashboard is displayed"),
                ScenarioStep("step-8-3", 3, "Verify admin-only widgets are visible", "All admin widgets render correctly"),
            ),
            expectedResult = "Admin dashboard loads with all management widgets.",
        ),
        Scenario(
            id = "scenario-9",
            title = "Payment processing via API",
            description = "Verify the payment gateway API processes transactions correctly.",
            type = ScenarioType.FUNCTIONAL,
            priority = ScenarioPriority.CRITICAL,
            tags = tags("payment", "api", "regression"),
            steps = listOf(
                ScenarioStep("step-9-1", 1, "Send POST /api/payments with valid card data", "201 Created response"),
                ScenarioStep("step-9-2", 2, "Verify transaction ID in response body", "Transaction ID is present"),
                ScenarioStep("step-9-3", 3, "Query GET /api/payments/{id}", "Transaction details match"),
            ),
            expectedResult = "Payment is processed and retrievable via API.",
        ),
        Scenario(
            id = "scenario-10",
            title = "Logout",
            description = "Verify that a user can log out and the session is terminated.",
            type = ScenarioType.SMOKE,
            priority = ScenarioPriority.LOW,
            tags = tags("auth", "smoke"),
            steps = listOf(
                ScenarioStep("step-10-1", 1, "Click the user avatar/menu", "User menu is displayed"),
                ScenarioStep("step-10-2", 2, "Click Logout", "User is redirected to the login page"),
                ScenarioStep("step-10-3", 3, "Try to access a protected page", "Redirected to login"),
            ),
            expectedResult = "Session is terminated and protected routes are inaccessible.",
        ),
    )

    val environments: List<Environment> = listOf(
        Environment(id = "env-qa", name = "QA", baseUrl = "https://qa.example.com"),
        Environment(id = "env-staging", name = "Staging", baseUrl = "https://staging.example.com"),
        Environment(id = "env-prod", name = "Production", baseUrl = "https://prod.example.com"),
    )

    val buildInfos: List<BuildInfo> = listOf(
        BuildInfo(id = "build-1", version = "2.4.1", branch = "main", commitHash = "abc1234"),
        BuildInfo(id = "build-2", version = "2.5.0-rc1", branch = "feature/checkout-redesign", commitHash = "def5678"),
    )

    val defects: List<Defect> = listOf(
        Defect(
            id = "defect-1",
            title = "Login fails with special characters in password",
            url = "https://tracker.example.com/issues/BUG-101",
            status = DefectStatus.OPEN,
        ),
        Defect(
            id = "defect-2",
            title = "Cart total rounding error on multi-currency checkout",
            url = "https://tracker.example.com/issues/BUG-205",
            status = DefectStatus.IN_PROGRESS,
        ),
        Defect(
            id = "defect-3",
            title = "Search timeout on catalogs with 10k+ products",
            url = "https://tracker.example.com/issues/BUG-312",
            status = DefectStatus.OPEN,
        ),
    )

    private val now: Instant = Clock.System.now()

    val testRuns: List<TestRun> = listOf(
        TestRun(
            id = "run-1",
            name = "Smoke Suite — v2.4.1",
            environmentId = "env-qa",
            buildInfoId = "build-1",
            status = RunStatus.COMPLETED,
            createdAt = now - 2.days,
            completedAt = now - 2.days + 3.hours,
            scenarioIds = listOf("scenario-1", "scenario-3", "scenario-5", "scenario-7", "scenario-10", "scenario-2"),
        ),
        TestRun(
            id = "run-2",
            name = "Regression Sprint 42",
            environmentId = "env-staging",
            buildInfoId = "build-1",
            status = RunStatus.IN_PROGRESS,
            createdAt = now - 1.days,
            scenarioIds = listOf("scenario-2", "scenario-4", "scenario-6", "scenario-9"),
        ),
        TestRun(
            id = "run-3",
            name = "Checkout Redesign Validation",
            environmentId = "env-qa",
            buildInfoId = "build-2",
            status = RunStatus.DRAFT,
            createdAt = now - 4.hours,
            scenarioIds = listOf("scenario-5", "scenario-6", "scenario-7", "scenario-9"),
        ),
    )

    val runResults: List<RunScenarioResult> = listOf(
        // Run 1 — Completed (4 passed, 1 failed, 1 blocked = 67% pass rate)
        RunScenarioResult("result-1", "run-1", "scenario-1", ResultStatus.PASSED, executedAt = now - 2.days + 30.minutes),
        RunScenarioResult("result-2", "run-1", "scenario-3", ResultStatus.PASSED, executedAt = now - 2.days + 1.hours),
        RunScenarioResult("result-3", "run-1", "scenario-5", ResultStatus.PASSED, executedAt = now - 2.days + 1.hours + 30.minutes),
        RunScenarioResult("result-4", "run-1", "scenario-7", ResultStatus.FAILED, comment = "Search returned 0 results for 'wireless headphones'", executedAt = now - 2.days + 2.hours, defectIds = listOf("defect-3")),
        RunScenarioResult("result-5", "run-1", "scenario-10", ResultStatus.PASSED, executedAt = now - 2.days + 2.hours + 30.minutes),
        RunScenarioResult("result-6", "run-1", "scenario-2", ResultStatus.BLOCKED, comment = "Blocked by login special chars bug", executedAt = now - 2.days + 3.hours, defectIds = listOf("defect-1")),

        // Run 2 — In Progress (2 passed, 1 failed, 1 not run)
        RunScenarioResult("result-7", "run-2", "scenario-2", ResultStatus.PASSED, executedAt = now - 20.hours),
        RunScenarioResult("result-8", "run-2", "scenario-4", ResultStatus.PASSED, executedAt = now - 18.hours),
        RunScenarioResult("result-9", "run-2", "scenario-6", ResultStatus.FAILED, comment = "Order confirmation page returned 500", executedAt = now - 16.hours, defectIds = listOf("defect-2")),
        RunScenarioResult("result-10", "run-2", "scenario-9", ResultStatus.NOT_RUN),

        // Run 3 — Draft (all not run)
        RunScenarioResult("result-11", "run-3", "scenario-5", ResultStatus.NOT_RUN),
        RunScenarioResult("result-12", "run-3", "scenario-6", ResultStatus.NOT_RUN),
        RunScenarioResult("result-13", "run-3", "scenario-7", ResultStatus.NOT_RUN),
        RunScenarioResult("result-14", "run-3", "scenario-9", ResultStatus.NOT_RUN),
    )
}
