---
name: android-clean-code-review
description: Review checklist for SOLID, mandatory dependency injection, small single-purpose functions, naming conventions, and testability/maintainability in this Android app (Kotlin, Clean Architecture, Hilt). Use when reviewing Kotlin changes in any layer.
---

# Clean code review: SOLID, DI, functions, naming, testability

Apply this to every changed Kotlin file in scope. Each finding needs `file:line`, the rule it breaks,
why it matters in this codebase, and a concrete fix (a short code snippet when it helps).

## SOLID

| Principle | What it means here | Typical violation |
|---|---|---|
| Single responsibility | DTO maps, data source calls the API, repo picks a source, use case holds one business action, ViewModel turns intents into state, composable renders | ViewModel formatting prices or building URLs; composable sorting/filtering data; data source applying business rules |
| Open/closed | New variants come from new subclasses of a sealed type or new implementations, not from editing `if/else` chains in several places | The same `when (type)` on strings repeated across layers |
| Liskov substitution | Implementations keep the interface's contract: data source and repo functions return `Flow<Resource<T>>` and report failures as `Resource.Error`, never by throwing | An impl that throws, returns a partial result silently, or ignores a parameter |
| Interface segregation | Small feature interfaces (`ProductsOnlineDataSource`, `CategoriesRepo`) | One large interface most callers use a fraction of |
| Dependency inversion | ViewModels depend on use cases, use cases on repo interfaces, repo impls on data source interfaces. `Impl` classes appear only in Hilt modules | ViewModel injecting `ApiServices` or a `RepoImpl`; UI importing `com.route.api` or `com.route.data` |

Layer rule checks (each must print nothing):

```bash
grep -rnE "import com\.route\.(api|data)\." app/src/main/java/com/route/firstapp
grep -rnE "import (android|androidx|retrofit2|com\.google\.gson)\." app/src/main/java/com/route/domain
grep -rnE "import com\.route\.api\." app/src/main/java/com/route/data
```

## Dependency injection is mandatory

- A class gets every collaborator through `@Inject constructor(...)`. ViewModels are `@HiltViewModel`.
- **A class must not create its own dependencies.** Flag `Xxx()`, `Xxx.Builder()` or `Xxx.create()`
  for services, repos, data sources, use cases, `Retrofit`, `OkHttpClient`, `Gson`, databases,
  `SharedPreferences`/DataStore or dispatchers anywhere except a Hilt `@Provides` function.
- Not violations: data classes, DTOs, domain models, UI state objects, `Modifier`, `MutableStateFlow`,
  `Channel`, builders inside `@Provides`.
- Bindings go in the layer's existing module (`ApiModule`, `DataSourcesModule`, `ReposModule`), with
  `@Binds` for interface → impl. No service locators, no `object` singletons holding dependencies or
  mutable state, no manual `lateinit` injection.
- Coroutine dispatchers are dependencies too. A hard-coded `Dispatchers.IO`/`Default` inside a class
  can't be swapped for a `TestDispatcher`. Recommend injecting them with a qualifier (for example
  `@IoDispatcher`) provided by a `DispatchersModule`; the project doesn't have one yet.

Search helpers (then filter to the files in scope and read each hit before reporting it):

```bash
grep -rnE "\b(Retrofit\.Builder|OkHttpClient(\.Builder)?|Gson|[A-Z][A-Za-z]*(RepoImpl|DataSourceImpl|UseCase|ViewModel|Repo|DataSource))\(" app/src/main/java
grep -rnE "Dispatchers\.(IO|Default|Main|Unconfined)" app/src/main/java
grep -rnE "GlobalScope|runBlocking|object [A-Z]\w*(Manager|Repository|Repo|Client|Service)\b" app/src/main/java
```

## Functions: small scope, one responsibility

- One function does one thing at one level of abstraction. Warning signs: "and" in an honest name,
  comments separating steps, more than 3 levels of nesting, more than ~30 lines (~50 for a layout
  composable), more than ~5 parameters (composables taking data plus callbacks are fine).
- Fix by extracting well-named private functions. `CategoriesViewModel` (`loadCategories`,
  `selectCategory`, `handleSubCategoryClick`) is the model to point to.
- No boolean flag parameters that switch behaviour in non-UI functions; split them into two functions.
  UI flags such as `isSelected` are fine.
- No side effects hidden in getters, mappers or composable bodies.

## Naming

- Kotlin conventions: classes, objects and composables in `PascalCase`; functions, properties and
  parameters in `camelCase`; constants in `UPPER_SNAKE_CASE`; booleans read as questions
  (`isSelected`, `hasNextPage`); backing properties `_name` and `private`.
- Functions are verbs (`loadProducts`, `handleProductClick`), classes are nouns, composables are
  nouns that describe UI (`CategoryItem`, `SubCategoriesSuccessState`).
- Where they differ, the project conventions in `AGENTS.md` win over generic Kotlin style: camelCase
  package folders (`productsSection`), `XxxDto`, `XxxOnlineDataSource(Impl)`, `XxxRepo(Impl)`,
  `GetXxxUseCase`, `XxxContract`, `XxxViewModel`, `XxxLoadingState` / `XxxErrorState` / `XxxSuccessState`.
- Flag misspelled or misleading names in new or touched code and resources (`getX` that also
  navigates; `data`, `item`, `temp` for domain concepts). Existing misspellings to fix when touched:
  `HomeSction.kt`, `ApiExecuter`, `Horinontal`, `most_purshable`, `cateogries`.

## Testable, scalable, maintainable

- Logic that can be unit tested lives in plain classes and functions (use cases, mappers,
  ViewModels), not in composables or Android framework classes.
- ViewModels expose only `states`, `events` and `doIntent` (see `android-mvi-review`), so a test sends
  intents and asserts states and events.
- No hidden globals: no mutable `object` state, no static caches, no direct `System.currentTimeMillis()`
  or `UUID.randomUUID()` in logic without an injectable provider.
- Mappers are pure and null-safe: `toDomain()` on the DTO returns `null` for invalid items and callers
  use `mapNotNull`.
- User-visible strings go in `values/strings.xml` and `values-ar/strings.xml`; colors come from
  `ui/theme`; no magic numbers repeated across files.
- A new feature adds files rather than editing unrelated features. Shared UI belongs in
  `CommonWidgets.kt` or `ui/theme`, not copy-pasted.
- New logic without unit tests is a finding (minor unless the change is risky). The project has no
  coroutine-test or mocking library yet, so suggest which to add instead of assuming one exists.
- Minor findings in touched files: dead code, commented-out blocks, `Log.e` debug logs, unused imports.
