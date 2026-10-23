---
name: android-mvi-review
description: Checks that every screen and section in this Android app follows its MVI pattern - Contract with State/Intent/Event, ViewModel where only doIntent is public, one file per UI state, a preview for every composable. Use when reviewing changes under app/src/main/java/com/route/firstapp/ui.
---

# MVI compliance review

Reference implementation: `app/src/main/java/com/route/firstapp/ui/categories/`. Rules come from the
"Presentation: MVI with Jetpack Compose" section of `AGENTS.md`.

## 1. Find the screens and sections in scope

```bash
grep -rlE "@HiltViewModel" app/src/main/java/com/route/firstapp/ui
grep -rnE "composable<AppRoutes\.\w+>" app/src/main/java/com/route/firstapp
```

Every destination registered in `AppNavHost` (`MainActivity.kt`) or `HomeNavHost` (`HomeScreen.kt`)
that loads data or reacts to user actions must be MVI. An empty placeholder screen is acceptable only
when the change says it's a placeholder.

## 2. Contract (`XxxContract.kt`)

- [ ] `interface XxxContract` with a nested `abstract class ViewModel : androidx.lifecycle.ViewModel()`
      exposing exactly `states: StateFlow<State>`, `events: Flow<Event>` and `doIntent(intent: Intent)`.
- [ ] `sealed class State` with `data object Loading`, `data class Success(...)` and
      `data class Error(message)`. Nested states are their own sealed classes.
- [ ] State is immutable: `val` only, `List` rather than `MutableList`, no `Context`, `View` or `Bitmap`.
- [ ] `sealed class Intent`: `Init(...)` plus one intent per user action, named after what happened
      (`CategorySelected`, `ProductClicked`).
- [ ] `sealed class Event`: one-shot effects (navigation, toast) carrying plain data, never an
      `AppRoutes`, `NavController` or `Context`.

## 3. ViewModel (`XxxViewModel.kt`)

- [ ] `@HiltViewModel class XxxViewModel @Inject constructor(private val ...)` extending `XxxContract.ViewModel`.
- [ ] **Only `doIntent` is public.** Every other function is `private`, injected dependencies are
      `private val`, `_states`/`_events` are `private`. The only other public members are the `states`
      and `events` overrides. A test then needs nothing but "send intent, assert state/event".
- [ ] Depends on use cases only: no repos, data sources, `ApiServices` or `Context`.
- [ ] `_states = MutableStateFlow<State>(State.Loading)` exposed with `asStateFlow()`;
      `_events = Channel<Event>(Channel.BUFFERED)` exposed with `receiveAsFlow()`.
- [ ] `doIntent` is a `when (intent)` that dispatches to private functions; at most a simple guard inline.
- [ ] Every `Resource` branch (Loading, Success, Error) maps to a state; no swallowed errors.
- [ ] Work runs in `viewModelScope`; a new load cancels the previous one when results could arrive out of order.
- [ ] A repeated `Init` doesn't wipe the user's state (see the guard in `CategoriesViewModel`).

Visibility check. It prints every non-private class member (4-space indent, so local `val`s inside
functions are skipped); each line must be one of the allowed ones:

```bash
grep -nE "^ {4}(override |suspend |open |internal |protected )*(fun|val|var) " <path/to/XxxViewModel.kt> | grep -vE "\bprivate\b"
```

Allowed: `override fun doIntent`, `override val states`, `override val events`. A constructor
parameter declared as plain `val` shows up here too, and is a finding.

## 4. Composables

- [ ] The entry file (`XxxScreen.kt` or `XxxSection.kt`) holds only the entry composable. It gets the
      ViewModel with `hiltViewModel(...)`, sends `Init` from `LifecycleEventEffect(ON_CREATE)`,
      collects `states` with `collectAsStateWithLifecycle()`, collects `events` in
      `LaunchedEffect(viewModel)`, and maps each `State` to one state composable.
- [ ] The UI calls the ViewModel only through `viewModel.doIntent(...)`.
- [ ] Events are not read with `collectAsState(...).value` / `collectAsStateWithLifecycle(null).value`
      in the composable body: the last event is kept as state and handled again on recomposition.
- [ ] One file per UI state: `XxxLoadingState.kt`, `XxxErrorState.kt`, `XxxSuccessState.kt`, and the
      same for nested states. Child composables live in the file of the state that uses them.
- [ ] State composables are stateless: data and lambdas in, no ViewModel parameter.
- [ ] Every composable except the `hiltViewModel()` entry has a `private` `@Preview` function named
      `<Composable>Preview`, wrapped in `MyPreview { }`, with one preview per meaningful variant.
- [ ] The error state's retry is wired to an `Intent`, never `onClick = {}`.
- [ ] A composable reused with different inputs passes `hiltViewModel(key = ...)`.
- [ ] Navigation goes through `onNavigate: (AppRoutes) -> Unit`; full-screen routes use the root
      `NavController` (`HomeNavHost`'s `onNavigate`).

Composables vs previews per file (entry files should show `previews=0`, all others at least one per composable):

```bash
for f in <ui files in scope>; do echo "$f composables=$(grep -cE '^fun [A-Z]' "$f") previews=$(grep -cE '^private fun \w+Preview\(' "$f")"; done
```
