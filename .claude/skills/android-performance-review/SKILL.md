---
name: android-performance-review
description: Performance checklist for this Android app - coroutine dispatchers and main-thread safety, Flow usage, Compose recomposition and lazy lists, image loading, memory leaks and network efficiency. Use when reviewing ViewModels, data sources, repositories or composables.
---

# Performance review

## Main thread and dispatchers

Nothing may block the main thread: no `runBlocking`, `Thread.sleep`, synchronous file/database/network
calls, or large parsing on `Dispatchers.Main`.

| Work | Dispatcher |
|---|---|
| Network, disk, database, `SharedPreferences`/DataStore, file reads | `Dispatchers.IO` |
| CPU-heavy: sorting, filtering or mapping large lists, big JSON payloads, image processing, diffing | `Dispatchers.Default` |
| Updating UI state | `Dispatchers.Main` (what `viewModelScope` uses) |

- Retrofit `suspend` calls are main-safe, so calling them from `viewModelScope` is fine. **The code
  around them is not**: in `safeApiCallV2` (`api/helpers/ApiExecuter.kt`) the `converter`
  (DTO → domain mapping) runs on whatever dispatcher collects the flow, which is Main when collected
  from `viewModelScope`. For non-trivial mapping, recommend `.flowOn(ioDispatcher)` (or `Default`) at
  the data source boundary.
- Use `withContext(dispatcher)` for one-shot work and `flowOn(dispatcher)` for flows (it changes the
  upstream only). Don't move whole ViewModels onto `Dispatchers.IO`.
- Dispatchers should be injected with a qualifier so tests can use a `TestDispatcher`. A hard-coded
  dispatcher is also a testability finding (see `android-clean-code-review`).
- No `GlobalScope` and no `CoroutineScope(...)` that is never cancelled. Use `viewModelScope`,
  `lifecycleScope` with `repeatOnLifecycle`, or Compose effect scopes.
- Cancel superseded work: keep the `Job` and cancel it before starting a new load (`selectCategory` in
  `CategoriesViewModel`), or use `flatMapLatest` / `collectLatest`.
- When the new state depends on the current one, write it with `_states.update { }` so concurrent
  updates aren't lost.
- One-shot events use `Channel` + `receiveAsFlow()` initialised once. A getter that calls
  `receiveAsFlow()` or `asStateFlow()` on every access creates a new flow each time, so a collector
  keyed on the flow restarts on every recomposition.

## Compose

- Flows are collected with `collectAsStateWithLifecycle()`, which stops in the background, not `collectAsState()`.
- No heavy work in the composition body. Sorting, filtering, formatting, `Regex` and date parsing
  belong in the ViewModel, or in `remember(key) { }` / `derivedStateOf` when they depend on UI state.
- Side effects only inside effect APIs (`LaunchedEffect`, `LifecycleEventEffect`, `DisposableEffect`),
  never directly in the body: navigation, logging, `Toast`.
- Lazy lists and grids use `items(list, key = { it.id })`. `items(list.size) { index -> }` gives no
  stable keys. Don't put a vertically scrolling lazy list inside `verticalScroll` without a fixed height.
- Read state as low in the tree as possible. Pass lambdas (`onClick: () -> Unit`) to children that only
  need an action.
- Keep composable parameters stable: immutable data classes, `List` not `MutableList`, no `var` fields.
- Avoid `IntrinsicSize` measurement inside large lazy lists; it adds a measure pass per item.
- Call `hiltViewModel()` at screen or section level, never inside `items { }`.

## Images

- `GlideImage` gets its size from its modifier, an explicit `contentScale`, and `loading`/`failure`
  placeholders for network images. Don't load full-resolution images into small tiles.
- Don't decode bitmaps or `painterResource` large bitmaps inside lists.

## Memory and lifecycle

- No `Context`, `View`, `Activity`, `NavController` or composable lambdas stored in ViewModels or singletons.
- Collectors are lifecycle-aware and cancelled with their scope.
- Large lists aren't duplicated across several state objects without need.

## Network efficiency

- No repeated requests for the same data on recomposition, tab switches or a re-sent `Init`.
- Paginated endpoints (`metadata.numberOfPages`): flag code that reads only page 1 when the UI needs all items.
- `ApiModule` logs every request and response body (`HttpLoggingInterceptor.Level.BODY`) in all build
  types. Logging should be limited to debug builds (`BuildConfig.DEBUG`).
- Runtime signal: `adb logcat -d | grep -E "Choreographer.*Skipped [0-9]+ frames"` after exercising a
  screen; many skipped frames mean too much work on the main thread.
