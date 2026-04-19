# TASKS.md — QA Scenario Manager Execution Pool

Format: ID | Type | Title | Done When | Status

| ID  | Type     | Title                                                       | Done When                                                                                      | Status |
|-----|----------|-------------------------------------------------------------|-----------------------------------------------------------------------------------------------|--------|
| T01 | bugfix   | Replace `Defect.status: String` with sealed class           | Model compiles, seed data updated, UI shows status as before, no String comparisons remain    | DONE   |
| T02 | test     | Unit tests for ScenarioListViewModel text search            | Tests cover empty query, partial match, no match                                               | DONE   |
| T03 | test     | Unit tests for smoke-only filter                            | Tests cover filter on/off and combined with search                                             | DONE   |
| T04 | test     | Unit tests for DashboardViewModel metrics                   | Tests verify pass-rate, failed-today count, smoke vs total calculations                        | DONE   |
| T05 | bugfix   | Empty state handling in Scenario List                       | Empty list shows explicit empty state UI with reason, no blank screen                          | DONE   |
| T06 | bugfix   | Null/blank comment edge case in Run Details                 | Empty comments render consistently, no null-related crashes or "null" text visible in UI       | DONE   |
| T07 | refactor | Replace `Defect.status: String` → ensure NO anti-patterns   | Grep confirms no `data class X(val status: String)` patterns across domain model               | DONE   |
| T08 | feature  | Add priority filter to Scenario List                        | Dropdown/toggle for priority, state hoisted to ViewModel, no logic in Composable               | DONE   |
| T09 | feature  | Add tag filter to Scenario List                             | Tag filter works alongside search and smoke-only filter simultaneously                          | DONE   |
| T10 | feature  | Show filtered count above Scenario List                     | UI displays "X of Y scenarios" and updates on each filter/search change                        | DONE   |
| T11 | feature  | Implement basic Settings screen (replace placeholder)       | Screen has at least 2 readable sections (e.g. app version, about), no "coming soon" text       | DONE   |
| T12 | feature  | Add sort by title / priority to Scenario List               | Sort toggle works without breaking existing filters                                            | DONE   |
| T13 | refactor | Add stable semantics to key interactive elements            | Buttons, filters, nav items have testTag or semantics modifier, CLAUDE.md checklist satisfied  | DONE   |
| T14 | refactor | Extract reusable StatusBadge for run and scenario statuses  | No duplicated badge rendering logic across screens, single source of truth                     | DONE   |
| T15 | docs     | Create `docs/smoke-checklist.md` for current MVP            | Covers Dashboard, Scenario List, Scenario Details, Run List, Run Details with steps            | DONE   |
| T16 | docs     | Create `docs/architecture-decisions.md`                     | Explains unidirectional data flow, ViewModel pattern, in-memory repo decision                  | DONE   |
| T17 | test     | Seed data integrity tests                                   | Tests verify expected count of scenarios, runs, environments, defects; no broken references    | DONE   |
| T18 | feature  | Add empty state for Run List                                | Empty run list shows friendly UI instead of blank screen                                       | DONE   |
