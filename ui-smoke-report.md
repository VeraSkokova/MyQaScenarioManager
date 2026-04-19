# UI Smoke Test Report

**Application:** QA Scenario Manager  
**Date:** 2026-04-19  
**Branch:** day3  
**Tester:** Automated (Claude Code + MCP Desktop Automation)

---

## Summary

| # | Scenario | Status |
|---|----------|--------|
| 1 | Dashboard & Navigation | **Pass** |
| 2 | Search functionality | **Pass** |
| 3 | Filter functionality | **Pass** |
| 4 | View Details & Navigation Back | **Pass** |
| 5 | Runs View | **Pass** |
| 6 | Dark Theme Toggle | **Pass** |

**Overall: 6/6 Passed**

---

## Detailed Results

### 1. Dashboard & Navigation

**Status:** Pass  
**Screenshot:** [01_dashboard.png](smoke_screenshots/01_dashboard.png), [02_scenarios_list.png](smoke_screenshots/02_scenarios_list.png)

**Verified:**
- Dashboard title is displayed
- Pass Rate metric shows `66,7%` (orange, below 80% threshold)
- Failed Today metric shows `1` (red)
- Smoke Scenarios metric shows `5 / 10`
- Recent Runs list displays 3 runs: Checkout Redesign Validation (DRAFT), Regression Sprint 42 (IN PROGRESS), Smoke Suite v2.4.1 (COMPLETED)
- Sidebar navigation to "Scenarios" works correctly

---

### 2. Search Functionality

**Status:** Pass  
**Screenshot:** [03_search_login.png](smoke_screenshots/03_search_login.png)

**Verified:**
- Typed "Login" into the search field
- List filtered to show only 2 matching scenarios:
  - "Login with valid credentials" (SMOKE, CRITICAL)
  - "Login with invalid credentials" (FUNCTIONAL, HIGH)
- Non-matching scenarios are hidden

---

### 3. Filter Functionality

**Status:** Pass  
**Screenshot:** [04_smoke_filter.png](smoke_screenshots/04_smoke_filter.png)

**Verified:**
- Cleared search by navigating away and back (Dashboard -> Scenarios)
- Toggled "Smoke only" filter chip (chip becomes highlighted/selected)
- List updated to show only 5 SMOKE-type scenarios:
  - Login with valid credentials (CRITICAL)
  - User registration (CRITICAL)
  - Add item to cart (HIGH)
  - Search products (MEDIUM)
  - Logout (LOW)
- All FUNCTIONAL scenarios are correctly filtered out

---

### 4. View Details & Navigation Back

**Status:** Pass  
**Screenshot:** [05_scenario_details.png](smoke_screenshots/05_scenario_details.png), [06_back_to_list.png](smoke_screenshots/06_back_to_list.png)

**Verified:**
- Clicked "Login with valid credentials" scenario
- Details screen displays:
  - Title: "Login with valid credentials"
  - Description: "Verify that a user can log in with a valid email and password."
  - Type badge: SMOKE
  - Priority badge: CRITICAL
  - Tag badges: auth, smoke
  - Steps section with 3 steps (action + expected result for each)
  - Expected Result: "User is authenticated and sees the dashboard."
- Back arrow navigates back to Scenario List successfully

---

### 5. Runs View

**Status:** Pass  
**Screenshot:** [07_runs_list.png](smoke_screenshots/07_runs_list.png), [08_run_details.png](smoke_screenshots/08_run_details.png)

**Verified:**
- Navigated to "Test Runs" via sidebar
- Run list displays 3 test runs with status badges:
  - Checkout Redesign Validation — DRAFT
  - Regression Sprint 42 — IN PROGRESS
  - Smoke Suite v2.4.1 — COMPLETED
- Opened "Checkout Redesign Validation" run details:
  - Environment: QA
  - Build: 2.5.0-rc1 (feature/checkout-redesign)
  - Created: 2026-04-19 14:20
  - Status badge: DRAFT
  - Scenario Results: 4 scenarios, all with "NOT RUN" status badges

---

### 6. Dark Theme Toggle

**Status:** Pass

**Verified:**
- Dashboard displays moon icon toggle button next to "Dashboard" title (light mode)
- Clicked the toggle — app switches to dark theme: dark background (#121212), light text, dark card surfaces
- Icon changes from moon (DarkMode) to sun (LightMode) in dark mode
- Metric cards, recent runs list, status badges, and sidebar all render correctly in dark mode
- Clicked the toggle again — app switches back to light theme with original colors
- Icon returns to moon, confirming full round-trip

---

## Notes

- **Compose Desktop Accessibility:** The Compose Desktop window does not expose its internal UI elements via the macOS Accessibility API (`AXUIElement`). The `ui_tree` tool only sees a single `AXWindow` node. All interaction was done via coordinate-based tapping and `screen_capture` visual verification.
- **Text Input Limitation:** Keystrokes (Backspace, Cmd+A) sent via AppleScript or MCP `input_key` do not reach the Compose Desktop text fields. Clearing the search field required navigating away and back. This is a known limitation of Skiko/AWT-based rendering in Compose Desktop and may affect future automated testing efforts.
- **Recommendation:** For robust UI automation of Compose Desktop apps, consider using Compose UI Testing framework (`createComposeRule()`) instead of OS-level accessibility-based tools.
