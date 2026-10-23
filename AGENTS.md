# AGENTS.md — FirstApp (Route E‑Commerce)

Context for AI coding agents working in this repository. Read this before changing code.

## What this app is

An Android e‑commerce client for the Route e‑commerce API (`https://ecommerce.routemisr.com/api/`).
Today it has a Home screen with a categories grid and three horizontal product rows (most selling,
lowest price, latest added), a bottom tab bar (Home / Categories / Favorites / Profile), a
Categories tab with a two‑level category → subcategory browser, and empty Product Details and
Products Catalog screens that are still to be built.

## Tech stack

| Concern | Choice |
|---|---|
| Language | Kotlin 2.4.21 (AGP 9 built‑in Kotlin; no `kotlin-android` plugin) |
| Build | Gradle Kotlin DSL, AGP 9.4.0, version catalog in `gradle/libs.versions.toml` |
| SDK | `compileSdk`/`targetSdk` 37, `minSdk` 24, Java 11 bytecode |
| UI | Jetpack Compose + Material 3 (Compose BOM 2026.02.01) |
| Images | Glide Compose (`GlideImage`, needs `@OptIn(ExperimentalGlideComposeApi::class)`) |
| Navigation | Navigation Compose 2.10 with type‑safe `@Serializable` routes |
| DI | Hilt 2.60 via KSP; `hiltViewModel()` from `androidx.hilt:hilt-lifecycle-viewmodel-compose` |
| Async | Kotlin Coroutines + `Flow`, `StateFlow`, `Channel` |
| Network | Retrofit 3 + Gson converter + OkHttp logging interceptor |
| Tests | Only the template JUnit4 / Espresso examples exist |

## Backend API (Route E‑Commerce)

- **Overview:** https://ecommerce.routemisr.com/ is Route Academy's public e‑commerce REST API for
  learning and testing.
- **Endpoint docs (Postman):** https://documenter.getpostman.com/view/5709532/2s93JqTRWN. Treat it as
  the source of truth for paths, query parameters, request bodies and JSON field names. Check it
  before adding or changing an endpoint or a DTO.
- The Postman page is rendered with JavaScript. From a terminal, fetch the collection as JSON instead
  (this worked on 2026‑10‑23):
  ```bash
  curl -s "https://documenter.gw.postman.com/api/collections/5709532/2s93JqTRWN?segregateAuth=true&versionTag=latest"
  ```
- To see the real response shape of a public `GET`, call it directly, e.g.
  `curl -s https://ecommerce.routemisr.com/api/v1/brands`.
- The base URL in code is `https://ecommerce.routemisr.com/api/` (`api/ApiModule.kt`), so Retrofit
  paths start with `v1/` or `v2/`.
- List responses share the envelope `{ results, metadata, data }`, which is `BaseResponse<T>`
  (`api/data/BaseResponse.kt`).
- User endpoints need a `token` header holding the token returned by sign‑in. Without it the API
  answers `"You are not logged in. Please login to get access"`. The app has no auth/header
  interceptor yet (`ApiModule` declares an unused `HeadersInterceptor` qualifier).

Endpoints from the Postman collection (🔒 = needs `token`; *used* = the app calls it today):

| Resource | Endpoints |
|---|---|
| Products | `GET v1/products` (*used*, with `sort`), `GET v1/products/:id` |
| Categories | `GET v1/categories` (*used*), `GET v1/categories/:id`, `GET v1/categories/:id/subcategories` (⚠️ ignores `:id` and returns every subcategory) |
| Subcategories | `GET v1/subcategories?category=:categoryId` (*used*, filters correctly), `GET v1/subcategories/:id`. No image field on any of the 60 subcategories; the UI shows a letter placeholder and `SubCategoryDto` reads `image` in case it's added |
| Brands | `GET v1/brands`, `GET v1/brands/:id` |
| Auth | `POST v1/auth/signup`, `POST v1/auth/signin`, `POST v1/auth/forgotPasswords`, `POST v1/auth/verifyResetCode`, `PUT v1/auth/resetPassword`, `GET v1/auth/verifyToken` 🔒, `PUT v1/users/changeMyPassword` 🔒, `PUT v1/users/updateMe` 🔒 |
| Wishlist 🔒 | `GET v1/wishlist`, `POST v1/wishlist` (`productId`), `DELETE v1/wishlist/:productId` |
| Addresses 🔒 | `GET v1/addresses`, `POST v1/addresses`, `GET v1/addresses/:id`, `DELETE v1/addresses/:id` |
| Cart v2 🔒 | `GET`/`POST`/`DELETE v2/cart`, `PUT`/`DELETE v2/cart/:productId`, `PUT v2/cart/applyCoupon` |
| Cart v1 🔒 | `GET`/`POST`/`DELETE v1/cart`, `PUT v1/cart/:productId` (`count`), `DELETE v1/cart/:id` |
| Reviews | `GET v1/reviews`, `GET v1/reviews/:id`, `GET v1/products/:productId/reviews`; 🔒 `POST v1/products/:productId/reviews`, `PUT`/`DELETE v1/reviews/:id` |
| Orders | 🔒 `POST v2/orders/:cartId` and `POST v1/orders/:cartId` (cash), 🔒 `POST v1/orders/checkout-session/:cartId?url=...` (Stripe), `GET v1/orders/`, `GET v1/orders/user/:userId` |

## UI design (Figma)

- **Design file:** https://www.figma.com/design/ZvbLqA2q9GudEss5hq0llH/Route-E-Commerce-App?node-id=564-542.
  It's the visual source of truth for screens, layout, spacing, colors and typography.
- File key `ZvbLqA2q9GudEss5hq0llH`, starting node `564:542` (the URL's `node-id=564-542`).
- With the Figma MCP server, call `get_metadata` to list the frames, then `get_design_context` or
  `get_screenshot` with that file key and a frame's node id. On 2026‑10‑23 every call failed with
  "you don't have edit access to this file" for the connected account. If it fails, ask the user for a
  screenshot or the node link of the frame you're building. Don't guess the layout.
- Treat Figma output as a reference, not code to paste. Build it from what exists: `ProductCard`,
  `HomeSectionTitle`, `BrandColor`, `MaterialTheme.typography`, `MyPreview`. If the design uses a
  color or text style that isn't in `ui/theme/` yet, add it there instead of inlining a literal.

## Build and run

```bash
./gradlew :app:compileDebugKotlin   # fastest check that the code compiles
./gradlew assembleDebug             # build the debug APK
./gradlew installDebug              # install on a connected device/emulator
./gradlew testDebugUnitTest         # JVM unit tests (app/src/test)
./gradlew lint                      # Android lint
```

Add dependencies through `gradle/libs.versions.toml` and reference them as `libs.xxx` in
`app/build.gradle.kts`. Don't hard‑code coordinates in the build script.

## Architecture: Clean Architecture in one Gradle module

There is a single Gradle module, `:app`. The Clean Architecture layers are **packages** under
`app/src/main/java/com/route/`, so the compiler won't enforce the dependency rule. You have to
keep to it yourself.

```
com.route
├── MyApplication.kt          @HiltAndroidApp
├── domain/                   Pure Kotlin. No Android, Retrofit, Gson or Compose imports.
│   ├── data/                 Domain models (Product, Category, ProductsSort) and Resource<T>
│   ├── repo/                 Repository interfaces (ProductsRepo, CategoriesRepo)
│   └── usecase/              Use cases (GetProductsUseCase, GetCategoriesUseCase)
├── data/                     Repository implementations + data source contracts
│   ├── dataSource/           Data source interfaces (ProductsOnlineDataSource, ...)
│   ├── repos/                Repo impls (ProductsRepoImpl, ...) that delegate to data sources
│   └── ReposModule.kt        Hilt @Binds Repo impl -> Repo interface
├── api/                      Remote data source implementation (Retrofit)
│   ├── ApiServices.kt        Retrofit endpoints
│   ├── ApiModule.kt          Hilt: OkHttp, Retrofit, ApiServices (Singleton)
│   ├── DataSourcesModule.kt  Hilt @Binds DataSource impl -> DataSource interface
│   ├── data/                 DTOs (ProductDto, CategoryDto, BaseResponse, ...) + mappers to domain
│   ├── dataSource/           DataSource impls (ProductsOnlineDataSourceImpl, ...)
│   └── helpers/ApiExecuter.kt safeApiCallV2(): wraps a call into Flow<Resource<R>>
└── firstapp/                 App / presentation layer
    ├── MainActivity.kt       @AndroidEntryPoint, RouteEcommerceApp(), AppNavHost
    └── ui/
        ├── AppRoutes.kt      @Serializable sealed class of every navigation route
        ├── CommonWidgets.kt  Shared composables (ProductCard)
        ├── theme/            FirstAppTheme, BrandColor, Typography, MyPreview
        ├── home/             HomeScreen, HomeNavHost, HomeTab, HomeSectionTitle
        │   ├── categoriesSection/   MVI section (older style)
        │   └── productsSection/     MVI section (the reference pattern)
        ├── categories/       Categories tab: MVI screen with one file per state (example of the split)
        ├── productDetails/   ProductDetailsScreen (empty, in progress)
        └── productsCatalog/  ProductsCatalogScreen (empty placeholder)
```

### Dependency rule

```
firstapp (UI) ──► domain ◄── data ◄── api
```

- `domain` depends on nothing except Kotlin, coroutines and `javax.inject`.
- `data` depends on `domain`. It owns the data source interfaces.
- `api` depends on `data` (it implements the data source interfaces) and `domain` (models, `Resource`).
- `firstapp` depends **only on `domain`**: use cases and models. It must never import
  `com.route.api.*` or `com.route.data.*`.
- DTOs never leave `api`. Map them to domain models inside the data source impl.

### Data flow for one request

```
Composable ──Intent──► ViewModel ──► UseCase ──► Repo (interface)
                                                   │
                                     RepoImpl ──► OnlineDataSource (interface)
                                                   │
                            OnlineDataSourceImpl ──► safeApiCallV2 { ApiServices.call() }
                                                   │
          Flow<Resource<DomainModel>>  ◄───────────┘   (Loading → Success | Error)
ViewModel maps Resource → Contract.State (StateFlow) and one‑shot Contract.Event (Channel)
```

`Resource<T>` (`domain/data/Resource.kt`) is the only result wrapper:
`Resource.Loading()`, `Resource.Success(data)`, `Resource.Error(throwable)`.
`safeApiCallV2(api, converter)` emits `Loading`, then `Success(converter(response.data))`, or
`Error(t)` on any exception.

## Dependency injection (Hilt)

| Module | Component | What it provides |
|---|---|---|
| `api/ApiModule.kt` | `SingletonComponent` | `HttpLoggingInterceptor`, `OkHttpClient`, `Retrofit`, `ApiServices` |
| `api/DataSourcesModule.kt` | `ViewModelComponent` | `@Binds` each `*OnlineDataSourceImpl` → `*OnlineDataSource` |
| `data/ReposModule.kt` | `ViewModelComponent` | `@Binds` each `*RepoImpl` → `*Repo` |

- Use cases, repo impls and data source impls use `@Inject constructor(...)`. Use cases need no module.
- ViewModels are `@HiltViewModel` with `@Inject constructor`.
- New bindings go into the existing module for their layer. Don't create a new module per feature.

## Presentation: MVI with Jetpack Compose

Every stateful UI unit (a screen or a self‑contained section) lives in its own package:

| File | Contents |
|---|---|
| `XxxContract.kt` | `interface XxxContract` holding an abstract `ViewModel` plus sealed `State`, `Intent` and `Event` |
| `XxxViewModel.kt` | `@HiltViewModel` implementation of `XxxContract.ViewModel` |
| `XxxScreen.kt` (`XxxSection.kt` for a section) | Only the stateful entry composable. It wires the ViewModel, collects states and events, and calls one state composable per `State` |
| `XxxLoadingState.kt` | `XxxLoadingState` and its preview |
| `XxxErrorState.kt` | `XxxErrorState(onRetryClick)` and its preview |
| `XxxSuccessState.kt` | `XxxSuccessState(...)` plus the child composables only it uses, each with a preview |

**When you implement a screen, put each UI state in its own file**, as in the table (see
`ui/categories/` for a full example). Nested states
get the same treatment: a `SubCategoriesState` gets `SubCategoriesLoadingState.kt`,
`SubCategoriesErrorState.kt` and `SubCategoriesSuccessState.kt`. A section follows the same split
whenever it has its own UI for a state. A state that only calls a shared composable (for example
`HomeCategoriesLoading`) doesn't need its own file.

Contract shape (see `ui/home/productsSection/ProductsSectionContract.kt`):

- `abstract class ViewModel : androidx.lifecycle.ViewModel()` exposing
  `states: StateFlow<State>`, `events: Flow<Event>` and `doIntent(intent: Intent)`.
- `State`: `data object Loading`, `data class Success(...)`, `data class Error(message)`.
- `Intent`: everything the UI can ask for. `Init(params)` starts loading. Add one per user action.
- `Event`: one‑shot side effects such as navigation or toasts.

ViewModel rules:

- `MutableStateFlow<State>(State.Loading)` exposed with `asStateFlow()`.
- `Channel<Event>(Channel.BUFFERED)` exposed with `receiveAsFlow()`.
- `doIntent` is a `when (intent)` that calls private functions. There is no other public API.
- Collect use case flows in `viewModelScope.launch { ... .collect { resource -> when (resource) ... } }`
  and map `Resource` onto `State`.
- Navigation is an `Event` that carries plain data (ids), never an `AppRoutes` object and never a `NavController`.

Composable rules:

- The entry composable takes `viewModel: XxxViewModel = hiltViewModel(...)` as a default parameter,
  plus `onNavigate: (AppRoutes) -> Unit` when it navigates.
- If one composable is used more than once on the same screen with different inputs (like
  `HorizontalProductCardRow` for each `ProductsSort`), pass a `key` to `hiltViewModel(key = ...)`.
  Otherwise every instance gets the same ViewModel.
- Kick off loading with `LifecycleEventEffect(Lifecycle.Event.ON_CREATE) { viewModel.doIntent(Intent.Init(...)) }`.
- Collect `states` with `collectAsStateWithLifecycle()`.
- Map `Event`s to `AppRoutes` in the composable and hand them to `onNavigate`. The `NavController`
  owner performs the navigation.
- Stateless child composables take data and lambdas only, never a ViewModel.
- **Every composable function gets a preview.** Put a `private` `@Preview(showBackground = true)`
  function named `<Composable>Preview` in the same file, wrapped in `MyPreview { }`, with realistic
  sample data. Add one preview per meaningful variant (selected and unselected, empty and filled
  list). The only exception is an entry composable that calls `hiltViewModel()`; the previews of its
  state composables cover it.
- User‑visible text goes in `res/values/strings.xml` and `res/values-ar/strings.xml`. Use the
  brand color from `ui/theme/Color.kt` (`BrandColor`), not new hex literals.

**Reference implementation:** `ui/home/productsSection/` (`ProductsSectionContract.kt`,
`ProductsViewModel.kt`, `HorizontalProductsList.kt`), backed by `GetProductsUseCase` →
`ProductsRepo` → `ProductsOnlineDataSource` → `ProductsOnlineDataSourceImpl` → `ApiServices.getProducts`.
The categories section works the same way but uses an older naming style
(`CategoriesState.CategoriesSuccessState`, loads in `init {}`). Follow the products section for
new code.

## Navigation

- All routes live in `ui/AppRoutes.kt` as `@Serializable` objects or data classes extending `AppRoutes`.
- There are two `NavHost`s:
  - `AppNavHost` in `MainActivity.kt` is the root graph for full‑screen destinations (`Home`, `ProductDetails`).
  - `HomeNavHost` in `HomeScreen.kt` is nested inside `HomeScreen` for the bottom tabs (`HomeTab`, `CategoriesTab`, ...).
- Register a full‑screen destination in `AppNavHost` and navigate to it with the **root**
  `NavController` (`appNavController` in `HomeScreen`). The inner tab controller only knows the tab routes.
  `HomeNavHost` takes an `onNavigate: (AppRoutes) -> Unit` that already calls the root controller;
  pass it to tabs (see `HomeTab`, `CategoriesTab`).
- Bottom tab clicks navigate with `popUpTo(startDestination) { saveState = true }`,
  `launchSingleTop` and `restoreState`, so there is one back stack entry per tab and each tab keeps
  its ViewModel and state. The selected tab is read from `currentBackStackEntryAsState()`.
- `HomeScreen` passes its Scaffold `innerPadding` to `HomeNavHost`, so tabs are laid out between the
  top bar and the bottom bar. The Scaffold sets `contentColor = Color.White`, so give text inside tabs
  an explicit color (usually `BrandColor`).
- Destination arguments come from the route data class, e.g. `AppRoutes.ProductDetails(productId)`.
  Read them with `backStackEntry.toRoute<AppRoutes.ProductDetails>()` in the `composable<...>` block.

## Conventions

- Package and folder names are camelCase (`productsSection`, `productDetails`, `dataSource`).
- Names: `XxxDto`, `XxxOnlineDataSource` / `XxxOnlineDataSourceImpl`, `XxxRepo` / `XxxRepoImpl`,
  `GetXxxUseCase`, `XxxContract`, `XxxViewModel`.
- DTO fields are all nullable with `= null` defaults and `@field:SerializedName("...")`.
  The DTO owns its mapper to the domain model (`ProductDto.toProduct()`, `CategoryDto.toCategory()`).
- An enum used as a query parameter has a domain enum (`ProductsSort`), an API enum that overrides
  `toString()` with the wire value (`ProductsSortTypeDto`), and an extension mapper (`toProductsSortDto()`).
- Repo, data source and use case functions return `Flow<Resource<T>>` and are **not** `suspend`.
  Only `ApiServices` functions are `suspend`.
- Composables take `modifier: Modifier = Modifier` as the first parameter.

## Known issues in the current code

Don't copy these into new code. Fix them only when your task touches them.

- `HomeCategoriesErrorState` text has no color, so it's invisible under the Scaffold's white content color.
- **Events handled in the composition body.** `HorizontalProductsList.kt` reads
  `events.collectAsStateWithLifecycle(null).value` and reacts inside the composable body, so the last
  event is kept as state and re‑handled on recomposition. New code collects events in
  `LaunchedEffect(viewModel) { viewModel.events.collect { ... } }`.
- **Retry button does nothing.** The products error state's "Try Again" has an empty `onClick`.
  Wire retry to an `Intent`.
- **Hard‑coded section title.** `ProductsSuccessState` always shows `R.string.most_purshable`, so
  all three rows have the same title.
- `values-ar/strings.xml` isn't translated yet and is missing `try_again`.
- `ProductDto.toProduct()` maps only `id`, `title` and `imageCover`. `ProductCard` shows a
  hard‑coded price and review count.
- Leftover debug code: `Log.e` calls and unused imports in several UI files.
- `productsSection/HorizontalProductsList.kt` was written before the one‑file‑per‑state and
  preview‑every‑composable rules. It keeps all states in one file and has no previews. Don't copy
  that layout; follow `ui/categories/` instead.

## Working agreements for agents

- Keep the layer boundaries above. If a change seems to need the UI importing `api` or `data`,
  add a use case instead.
- Make the smallest change that does the job. Don't reformat or reorganise unrelated files.
  The working tree often has uncommitted work in progress.
- After code changes, run `./gradlew :app:compileDebugKotlin` and report the result.
- For a new feature end to end, use the `android-feature-coder` agent
  (`.claude/agents/android-feature-coder.md`). It has the step‑by‑step recipe and templates.
