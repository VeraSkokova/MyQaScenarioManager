---
description: Core architecture rules (UDF, Kotlin Coroutines, StateFlow) for the QA Scenario Manager project.
---

# Architecture Rules
- Unidirectional data flow (state down, events up). Business logic lives in ViewModels/UseCases, never in Composables.

# State & Events
- Immutable public state (`data class`, `val`). No `var` inside data class.
- Typed events (`sealed class`). No arbitrary strings for status.

# Kotlin Best Practices
- Use Coroutines + StateFlow.
- Return `Result<T>` for error handling. No silent exception swallowing.
- No Java-style Kotlin.