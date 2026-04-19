# Smoke Test Checklist — QA Scenario Manager MVP

## Dashboard
- [ ] Dashboard loads with title "Dashboard"
- [ ] Pass Rate metric card displays percentage
- [ ] Failed Today metric card displays count
- [ ] Smoke Scenarios metric card displays "X / Y" format
- [ ] Recent Runs section lists test runs
- [ ] Clicking a recent run navigates to Run Details
- [ ] Theme toggle button switches between light and dark mode

## Scenario List
- [ ] Scenario List loads with title "Scenarios"
- [ ] All seed scenarios are displayed
- [ ] Search field filters scenarios by title and description
- [ ] Smoke only filter chip toggles smoke-only view
- [ ] Priority filter chips (LOW, MEDIUM, HIGH, CRITICAL) filter correctly
- [ ] Tag filter chips filter by tag
- [ ] Filters work simultaneously (search + smoke + priority + tag)
- [ ] Filtered count "X of Y scenarios" updates on filter changes
- [ ] Sort toggle cycles through Title A-Z, Title Z-A, Priority High-Low, Priority Low-High
- [ ] Empty state shows contextual message when no results match
- [ ] Clicking a scenario row navigates to Scenario Details

## Scenario Details
- [ ] Back button returns to Scenario List
- [ ] Title displays scenario name
- [ ] Description text is visible
- [ ] Type badge shows SMOKE or FUNCTIONAL with correct color
- [ ] Priority badge shows priority level with correct color
- [ ] Tags are displayed as badges
- [ ] Steps are listed with step number, action, and expected result
- [ ] Expected Result section shows overall expected result
- [ ] Linked Defects section shows defects (when present) with status and URL

## Run List
- [ ] Run List loads with title "Test Runs"
- [ ] All seed runs are displayed
- [ ] Each run card shows name, environment, scenario count, date, and status badge
- [ ] Clicking a run card navigates to Run Details

## Run Details
- [ ] Back button returns to Run List (or previous screen)
- [ ] Title displays run name
- [ ] Info card shows Environment, Build version/branch, Created date, and Status badge
- [ ] Completed date shown when run is completed
- [ ] Scenario Results section lists all results
- [ ] Each result row shows scenario title, status badge, and execution timestamp
- [ ] Comments are displayed when present (blank comments are hidden)
- [ ] Status dropdown allows changing result status (PASSED, FAILED, BLOCKED, NOT_RUN)
- [ ] Changing status preserves existing comment

## Settings
- [ ] Settings screen loads with title "Settings"
- [ ] Application section shows App Name, Version, and Platform
- [ ] About section shows Data Storage, Architecture, and description text

## Navigation
- [ ] Sidebar has four items: Dashboard, Scenarios, Runs, Settings
- [ ] Clicking each sidebar item navigates to the correct screen
- [ ] Active sidebar item is highlighted
- [ ] Back navigation works from detail screens
