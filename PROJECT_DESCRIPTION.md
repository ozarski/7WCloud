# The7WCloud — Unofficial 7 Wonders Board Game Score Tracker

> **Currently Working On:** Admin Panel — extended with "Delete User's Games", "Delete User's Players", and "Delete User Account" actions (with confirmation popups, per-action spinners, cross-screen game list & player list refresh via savedStateHandle). Backed by three Supabase RPCs (`delete_user_games`, `delete_user_players`, `delete_user_account`). See `app/src/main/java/com/example/the7wcloud/ui/adminScreen/`, `supabase-migration.sql` (section 13).

## Overview

An **Android mobile application** built with **Kotlin** and **Jetpack Compose** (Material 3) that serves as an unofficial companion for the board game **7 Wonders** and its expansions (Leaders, Cities, Armada). The app allows players to **track game scores**, **manage player profiles**, and **view historical game details with statistics**. It uses **Supabase** as its backend-as-a-service for authentication, data storage, and Row-Level Security (RLS).

**Root project name:** `The7WCloud` (defined in `settings.gradle.kts`)

---

## Tech Stack

| Layer | Technology | Version |
|---|---|---|
| **Language** | Kotlin | 2.2.0 |
| **UI Framework** | Jetpack Compose + Material 3 | Compose BOM (via libs.versions.toml) |
| **Architecture** | MVVM with Clean Architecture (domain / data / ui layers) | — |
| **Dependency Injection** | Dagger Hilt | 2.57 |
| **Backend / Database** | Supabase (PostgreSQL + PostgREST + Auth) | Supabase Kotlin SDK 3.1.1 |
| **Networking** | Ktor Client (OkHttp engine) | via Supabase SDK |
| **Serialization** | Kotlinx Serialization | 1.9.0 |
| **Navigation** | Jetpack Navigation Compose | 2.9.2 |
| **Build System** | Gradle | 8.13 (Kotlin DSL) |
| **Min SDK / Target SDK** | Android 8.0 (API 26) / Android 16 (API 36) | — |
| **Authentication** | Google Sign-In via Android Credential Manager + Supabase Auth (IDToken flow) | — |
| **Version Catalog** | `gradle/libs.versions.toml` | centralized dependency versions |

---

## Project Structure

```
7WCloud/
├── build.gradle.kts                          # Root build config (plugin declarations)
├── settings.gradle.kts                       # Project settings, module includes
├── gradle.properties                         # JVM args, AndroidX, Kotlin style
├── gradle/
│   ├── libs.versions.toml                    # Version catalog (ALL dependency versions)
│   └── wrapper/
│       └── gradle-wrapper.properties         # Gradle 8.13 distribution
├── gradlew / gradlew.bat                     # Gradle wrapper scripts
├── local.properties                          # Local Android SDK path (gitignored)
├── supabase-migration.sql                    # Full DB schema + RLS policies + RPC functions
├── database.db                               # Local SQLite database (dev artifact)
├── README.md                                 # Brief README with screenshots only
└── app/
    ├── build.gradle.kts                      # Module build config (SDK versions, deps, buildConfigFields)
    ├── proguard-rules.pro
    └── src/main/
        ├── AndroidManifest.xml
        └── java/com/example/the7wcloud/
            ├── SevenWondersApp.kt            # @HiltAndroidApp Application class
            ├── MainActivity.kt               # Entry point: edge-to-edge + auth-gated NavHost
            ├── di/
            │   ├── NetworkModule.kt          # Provides SupabaseClient singleton (Auth + Postgrest + KotlinXSerializer)
            │   └── RepositoryModule.kt       # Binds repository interfaces -> implementations (@Binds)
            ├── domain/
            │   ├── model/                    # Pure Kotlin domain models (no framework deps)
            │   │   ├── AuthState.kt          # sealed interface: Loading | Unauthenticated | Authenticated(user)
            │   │   ├── UserModel.kt          # id, email, displayName, photoUrl
            │   │   ├── GameModel.kt          # id, date, playerScores (list of name->score pairs), isPrivate
            │   │   ├── GameDetailsModel.kt   # id, date, isPrivate, userId, playerScores (list of PlayerResultModel)
            │   │   ├── PlayerModel.kt        # id, name, isPrivate, wins, games, topScore, avgPlacement
            │   │   ├── PlayerResultModel.kt  # playerID, playerName, totalScore, placement, scores (list of PointType->Int? pairs)
            │   │   ├── PointTypeModel.kt     # PointTypeInterface + 4 enums for scoring categories
            │   │   ├── PlayerPointTypeModel.kt # playerID, playerName, pointType, value (String)
            │   │   ├── AdminUserModel.kt       # id, email, displayName, photoUrl, role
            │   │   └── AddPlayerToGameModel.kt # id, name, isPlaying, ordinal (turn order)
            │   └── repository/               # Interface definitions only
            │       ├── AdminRepository.kt     # getAllUsers(), setUserRole(), deleteUserPlayers(), deleteUserGames(), deleteUserAccount()
            │       ├── AuthRepository.kt      # observeAuthState(), signInWithGoogle(), signOut()
            │       ├── GameRepository.kt      # getGames(), getGameDetails(), addGame(), deleteGame(), updateGame(), isAdmin()
            │       ├── PlayerRepository.kt    # getPlayersWithStats(), getAllPlayers(), addPlayer(), playerExists(), deletePlayer(), updatePlayer()
            │       └── PlayerResultRepository.kt # addPlayerResult(), deletePlayerResult(), deletePlayerResultsForGame()
            ├── data/
            │   ├── model/                    # Serialization DTOs (@Serializable data classes)
            │   │   ├── GameDto.kt            # id, createdAt, isPrivate, userId, date
            │   │   ├── PlayerDto.kt          # id, createdAt, name, isPrivate, userId, deleted
            │   │   ├── PlayerResultDto.kt    # playerID, gameID + 12 score fields + totalPoints, placement, userId
            │   │   ├── AdminUserDto.kt        # id, email, display_name, photo_url, role + toDomainModel()
            │   │   ├── GameWithPlayerDetailsDto.kt  # Full detail DTO with toGameDetailsModel() + toPlayerResultModel()
            │   │   ├── GameWithResultsDto.kt # gameID, playerID, name, date, totalScore
            │   │   └── PlayerWithStatsDto.kt # id, name, avgPlacement, wins, games, topScore + toDomainModel()
            │   ├── remote/
            │   │   └── SupabaseConfig.kt     # Reads BuildConfig.SUPABASE_URL, SUPABASE_ANON_KEY, WEB_CLIENT_ID
            │   └── repository/               # Supabase PostgREST implementations
            │       ├── AdminRepositoryImpl.kt    # RPC calls for list_users, set_user_role, delete_user_players, delete_user_games, delete_user_account
            │       ├── AuthRepositoryImpl.kt     # Google IDToken sign-in, sessionStatus flow observation
            │       ├── GameRepositoryImpl.kt     # Parallel Supabase queries (games + results + players), RPC calls for mutations
            │       ├── PlayerRepositoryImpl.kt   # Players + stats computation, RPC calls for mutations
            │       └── PlayerResultRepositoryImpl.kt # Direct insert/delete + RPC for batch delete
            └── ui/
                ├── Screens.kt                # Navigation route enum: Login, MainTabs, AddGame, GameDetails, AdminPanel
                ├── theme/                    # Custom Material 3 theme
                │   ├── Color.kt              # BaseColors + PointTypeColors (per-category colors)
                │   ├── Dimens.kt             # Spacing, font sizes, component dimensions
                │   ├── Type.kt               # Typography definitions
                │   └── Theme.kt              # The7WondersTheme composable
                ├── base/                     # Reusable UI components
                │   ├── BaseBackground.kt     # Themed background wrapper
                │   ├── BaseCard.kt           # Reusable card composable
                │   ├── BaseCheckbox.kt       # Custom checkbox
                │   ├── BaseInputField.kt     # Text input field
                │   ├── BaseFloatingActionButton.kt
                │   ├── BasePopupContainer.kt # Modal popup shell
                │   ├── PrimaryButton.kt      # Styled primary action button
                │   ├── LoadingScreen.kt      # Centered CircularProgressIndicator
                │   ├── ErrorWidget.kt        # Error message display
                │   ├── AlertPopup.kt         # Alert dialog composable
                │   └── ConfirmationPopup.kt  # Confirm/cancel dialog
                ├── authScreen/
                │   ├── LoginScreen.kt        # Google Sign-In button UI
                │   └── AuthViewModel.kt      # Auth state observation + signIn/signOut
                ├── tabsScreen/               # Main tabbed interface
                │   ├── MainTabs.kt           # Tab enum (Games, Players)
                │   ├── MainTabsScreen.kt     # Tab host with bottom bar + FAB
                │   ├── MainTabsState.kt      # UI state data class
                │   ├── MainTabsViewModel.kt  # Orchestrates games + players loading
                │   ├── SettingsPopup.kt      # Settings menu (sign out, admin panel)
                │   ├── TabsBar.kt            # Bottom navigation bar
                │   ├── gamesTab/             # Games list tab
                │   │   ├── GameListScreen.kt
                │   │   ├── GameListViewModel.kt  # Loads + deletes games
                │   │   ├── GameListState.kt
                │   │   └── GameListItem.kt
                │   └── playersTab/           # Players list tab
                │       ├── PlayerListScreen.kt
                │       ├── PlayerListViewModel.kt # Loads + adds players
                │       ├── PlayerListState.kt
                │       ├── PlayerListItem.kt
                │       ├── addPlayer/        # Add player popup
                │       └── editPlayer/       # Edit/delete player popup
                ├── addGameScreen/            # Multi-step game creation wizard
                │   ├── AddGameScreen.kt      # Phase-based screen host (GamePhase enum)
                │   ├── AddGameViewModel.kt   # Full game creation state machine
                │   ├── AddGameState.kt       # State: availablePlayers, selectedPlayers, pointQueue, confirmedPoints, DLC toggles, gamePhase
                │   ├── playerSelection/      # Step 1: Select players + set turn order
                │   ├── pickDLCs/             # Step 2: Toggle expansions + privacy
                │   ├── inputPoints/          # Step 3: Score input per category per player
                │   │   ├── PointInputScreen.kt
                │   │   ├── GreenPointsButton.kt
                │   │   └── GreenCardsCalculatorPopup.kt  # Scientific symbol calculator
                │   ├── confirmation/         # Step 4: Review all scores
                │   └── results/              # Step 5: Podium/leaderboard after saving
                ├── adminScreen/              # Admin panel (user management)
                │   ├── AdminPanelScreen.kt    # Full-screen user list with role toggle + delete games/players/account buttons, confirmation popups, BackHandler for cross-screen sync
                │   ├── AdminPanelViewModel.kt # Loads users, handles promote/demote, deleteUserPlayers, deleteUserGames, deleteUserAccount via AdminRepository
                │   └── AdminPanelState.kt     # UI state: users, loading, errors, action loading, per-action loading tracking, confirmation visibility, gamesDeleted & playersDeleted flags
                ├── gameDetailsScreen/        # Game detail view
                │   ├── GameDetailsScreen.kt
                │   ├── GameDetailsViewModel.kt # Loads game details, toggles privacy, deletes
                │   ├── GameDetailsState.kt
                │   └── PlayerResultsItem.kt  # Per-player score breakdown card
                └── util/
                    └── ErrorMapper.kt        # Maps exceptions to user-friendly error strings
```

---

## Domain Models (Detailed)

### AuthState
```kotlin
sealed interface AuthState {
    data object Loading : AuthState
    data object Unauthenticated : AuthState
    data class Authenticated(val user: UserModel) : AuthState
}
```
- `Loading`: Initial state while Supabase session is being checked
- `Unauthenticated`: No valid session; shows login screen
- `Authenticated`: Valid session; shows main app UI

### UserModel
```kotlin
data class UserModel(val id: String, val email: String?, val displayName: String?, val photoUrl: String?)
```

### GameModel
```kotlin
data class GameModel(
    val id: Long?,              // null when creating a new game
    val date: Long,             // epoch millis (Calendar.getInstance().timeInMillis)
    val playerScores: List<Pair<String, Int>>,  // (playerName, totalScore) -- used for list display only
    val isPrivate: Boolean = false
)
```
Used for the games list (summary view) and CRUD operations. The `playerScores` list is populated by joining `PlayerResults` with `Players` on the server side at read time. The `toGameDto()` extension function converts it to a `GameDto` for serialization.

### GameDetailsModel
```kotlin
data class GameDetailsModel(
    val id: Long? = null,
    val date: Long? = null,
    val isPrivate: Boolean = false,
    val userId: String? = null,
    val playerScores: List<PlayerResultModel>
)
```
Used for the full game detail view. Includes every PlayerResult with score breakdown.

### AdminUserModel
```kotlin
data class AdminUserModel(
    val id: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?,
    val role: String  // "user" or "admin"
)
```
Used in the admin panel to display all registered users. The `role` field comes from the `Roles` table; users without a `Roles` entry default to `"user"` via `COALESCE` in the `list_users` RPC.

### PlayerModel
```kotlin
data class PlayerModel(
    val id: Long? = null,
    val name: String,
    val isPrivate: Boolean = false,
    val wins: Int? = null,
    val games: Int? = null,
    val topScore: Int? = null,
    val avgPlacement: Double? = null
)
```
Stats (`wins`, `games`, `topScore`, `avgPlacement`) are computed client-side by grouping `PlayerResults` by `playerID`.

### PlayerResultModel
```kotlin
data class PlayerResultModel(
    val playerID: Long,
    val playerName: String,
    val totalScore: Int,
    val placement: Int,
    val scores: List<Pair<PointTypeInterface, Int?>>
)
```
The `scores` list contains one entry per scoring category (base + enabled DLCs). Null values represent categories not used in that game.

### PointTypeModel (Scoring Categories)
```kotlin
interface PointTypeInterface {
    val pointName: String
    val color: Color
    val icon: Int
}

enum class BasePointTypes : PointTypeInterface {
    Wonder, Military, Gold, Blue, Yellow, Green, Purple
}

enum class ArmadaPointTypes : PointTypeInterface {
    NavalConflicts, IslandCards, NavalVictory
}

enum class CityPointTypes : PointTypeInterface {
    CityCards
}

enum class LeaderPointTypes : PointTypeInterface {
    LeaderCards
}
```
Each enum entry defines: display name, category color (from `PointTypeColors`), and drawable icon resource. The `getPlayerResultsIndex()` companion function computes a flat index across all categories for queue management.

### AddPlayerToGameModel
```kotlin
data class AddPlayerToGameModel(
    val id: Long,
    val name: String,
    val isPlaying: Boolean,
    val ordinal: Int?  // turn order (1-based)
)
```
Used in the game creation flow for player selection with turn ordering.

### PlayerPointTypeModel
```kotlin
data class PlayerPointTypeModel(
    val playerID: Long,
    val playerName: String,
    val pointType: PointTypeInterface,
    val value: String  // user input (empty string or numeric string)
)
```
Represents a single score input field: one player + one point type + their entered value. These are processed in a queue during the point input phase.

---

## Data Models (DTOs -- @Serializable)

### GameDto
```kotlin
@Serializable
data class GameDto(
    val id: Long? = null,
    val createdAt: String? = null,   // @SerialName("created_at")
    val isPrivate: Boolean = false,  // @SerialName("isPrivate")
    val userId: String? = null,      // @SerialName("userId")
    val date: Long
)
```

### PlayerDto
```kotlin
@Serializable
data class PlayerDto(
    val id: Long? = null,
    val createdAt: String? = null,   // @SerialName("created_at")
    val name: String,
    val isPrivate: Boolean = false,  // @SerialName("isPrivate")
    val userId: String? = null,      // @SerialName("userId")
    val deleted: Boolean = false
)
```

### PlayerResultDto (12 score columns)
```kotlin
@Serializable
data class PlayerResultDto(
    val playerID: Long,              // @SerialName("playerID")
    val gameID: Long,                // @SerialName("gameID")
    val wonderPoints: Int,           // base category
    val militaryPoints: Int,         // base category
    val moneyPoints: Int = 0,        // base category (Gold)
    val blueCardsPoints: Int = 0,    // base category
    val yellowCardsPoints: Int = 0,  // base category
    val greenCardsPoints: Int = 0,   // base category
    val purpleCardsPoints: Int = 0,  // base category
    val cityCardsPoints: Int? = null,         // Cities DLC (nullable -- null when DLC not used)
    val leaderCardsPoints: Int? = null,       // Leaders DLC (nullable)
    val navalConflictsPoints: Int? = null,    // Armada DLC (nullable)
    val islandCardsPoints: Int? = null,       // Armada DLC (nullable)
    val navalVictoryPoints: Int? = null,      // Armada DLC (nullable)
    val totalPoints: Int,
    val placement: Int,
    val userId: String? = null
)
```
- Base game categories always have values (non-null, default 0)
- DLC categories are nullable -- null when the expansion wasn't used, a score when it was

### GameWithPlayerDetailsDto
Full-join DTO used for the game detail screen. Contains all score fields + `date`, `name` (player name). Has companion `toGameDetailsModel()` factory.

---

## Supabase Database Schema

### Tables

**Games** -- `id` (bigint, PK), `created_at` (timestamptz), `userId` (uuid -> auth.users), `isPrivate` (bool, default false), `date` (bigint)

**Players** -- `id` (bigint, PK), `created_at` (timestamptz), `name` (text), `isPrivate` (bool, default false), `userId` (uuid -> auth.users), `deleted` (bool, default false)

**PlayerResults** -- `id` (bigint, PK), `created_at` (timestamptz), `playerID` (bigint -> Players.id), `gameID` (bigint -> Games.id), `userId` (uuid -> auth.users), `wonderPoints`, `militaryPoints`, `moneyPoints`, `blueCardsPoints`, `yellowCardsPoints`, `greenCardsPoints`, `purpleCardsPoints`, `cityCardsPoints` (nullable), `leaderCardsPoints` (nullable), `navalConflictsPoints` (nullable), `islandCardsPoints` (nullable), `navalVictoryPoints` (nullable), `totalPoints`, `placement`

**Roles** -- `userId` (uuid, PK -> auth.users), `role` (text, check: 'user' | 'admin')

### RLS Policies
- **Games**: Public games visible to all; private games only to owner; admins see all
- **Players**: Public + own private + players appearing in public games; admins see all
- **PlayerResults**: Visible through game visibility (public games or own games); admins see all
- **All tables**: INSERT only for authenticated users with `userId = auth.uid()`
- UPDATE/DELETE: owner only, or admin

### RPC Functions
```sql
delete_game(game_id BIGINT)              -- Deletes player results + game (ownership-checked)
update_game_privacy(game_id BIGINT, is_private BOOLEAN)  -- Toggles privacy
delete_player(player_id BIGINT)          -- Soft-deletes (sets deleted = true)
update_player(player_id BIGINT, player_name TEXT, is_private BOOLEAN)
delete_player_results_for_game(game_id BIGINT)
is_admin() RETURNS boolean              -- Checks Roles table
list_users() RETURNS TABLE (id, email, display_name, photo_url, role)  -- All users (admin-only)
set_user_role(target_user_id UUID, new_role TEXT)  -- Promote/demote user (admin-only)
delete_user_players(target_user_id UUID) -- Soft-deletes all Players owned by user (admin-only)
delete_user_games(target_user_id UUID)   -- Deletes all PlayerResults + Games owned by user (admin-only)
delete_user_account(target_user_id UUID) -- Deletes PlayerResults + Games + Players + Roles + auth.users (admin-only)
```

### Triggers
`set_games_user_id`, `set_players_user_id`, `set_player_results_user_id` -- auto-set `userId = auth.uid()` on INSERT via `SECURITY DEFINER` function `set_user_id()`.

---

## Key Architecture Patterns

### Clean Architecture Layers

1. **domain/** -- Pure Kotlin. Contains interfaces (repositories) and data classes (models). Zero Android framework dependencies. All business logic lives in ViewModels (ui layer) that depend on these interfaces.

2. **data/** -- Contains DTOs (@Serializable), Supabase SDK calls, and repository implementations. Repository implementations map between DTOs and domain models. The `SupabaseClient` is injected as a singleton via Hilt.

3. **ui/** -- Jetpack Compose screens + ViewModels. Each screen has a corresponding ViewModel and often a state data class. ViewModels use `mutableStateOf()` for state (not `StateFlow`). Error handling uses `mapToUserMessage()` to convert exceptions to user-facing strings.

### Dependency Injection (Hilt)
- `SevenWondersApp` -- `@HiltAndroidApp` Application class
- `MainActivity` -- `@AndroidEntryPoint` Activity
- All ViewModels -- `@HiltViewModel` with `@Inject constructor`
- `NetworkModule` (`@Module @InstallIn(SingletonComponent::class)`) -- provides `SupabaseClient` singleton
- `RepositoryModule` (`@Module @InstallIn(SingletonComponent::class)`) -- binds all 5 repository interfaces to their implementations

### State Management
- Each ViewModel holds a `mutableStateOf<XxxState>()` exposed as `val state: State<XxxState>`
- Compose screens observe `viewModel.state.value` and react accordingly
- No StateFlow, no Flow in UI -- just Compose `State` objects
- Auth state uses a `Flow<AuthState>` from `sessionStatus` observed in `AuthViewModel.init{}`

### Navigation
- `NavHost` with 4 routes: `MainTabs`, `AddGame` (slide-up animation), `GameDetails/{id}` (slide-up animation), `AdminPanel` (slide-up animation)
- `Screens` enum defines route strings + a `GAME_DETAILS_ID_PARAM` companion constant
- Auth gating at the `AppNavigation()` level via `when(authState)`

### Error Handling
```kotlin
fun mapToUserMessage(e: Throwable): String = when (e) {
    is PostgrestRestException -> when (e.code) {
        "42501" -> "You don't have permission to perform this action"
        "23505" -> "A player with this name already exists"
        else -> e.error
    }
    is UnauthorizedRestException -> "You don't have permission to perform this action"
    is UnknownHostException, is ConnectException -> "Connection error"
    is SocketTimeoutException -> "Connection timed out"
    else -> e.message ?: "An unexpected error occurred"
}
```
- Error messages surface via `ErrorWidget` composable
- UI-level error cleared by user interaction

---

## App Flow & Screen Details

### Authentication Flow
1. App starts -> `AuthState.Loading` -> `LoadingScreen`
2. `AuthViewModel` observes `authRepository.observeAuthState()` (Supabase `sessionStatus` flow)
3. If no session -> `AuthState.Unauthenticated` -> `LoginScreen` with Google Sign-In button
4. User taps sign-in -> Android Credential Manager returns ID token -> `authRepository.signInWithGoogle(idToken)` -> Supabase `auth.signInWith(IDToken)` with Google provider
5. On success -> `AuthState.Authenticated` -> main app UI
6. Session persists across app restarts via Supabase auto-refresh

### Game Creation Wizard (5 Phases)
1. **PlayerSelection**: Load all non-deleted players from Supabase. Tap to toggle `isPlaying`, auto-assigns `ordinal` (1-based turn order, max 7). Supports deselection with ordinal rebalancing.

2. **DLCSelection**: 3 toggle switches for expansions (Cities, Leaders, Armada) + 1 toggle for game privacy. Confirming builds the point input queue.

3. **PointInput**: Queue-based input system. Each `PlayerPointTypeModel` (player + category) is processed sequentially. Base categories always appear first (Wonder, Military, Gold, Blue, Yellow, Green, Purple), then DLC categories in order (Leaders -> Cities -> Armada). Each category has a custom icon and color. The Green category shows a special button that opens the `GreenCardsCalculatorPopup` -- a calculator that computes green card points from scientific symbol counts using the formula: `7 * min(sets) + sum(symbol^2)` for each of cogwheels, tablets, and compasses.

4. **Confirmation**: Shows all entered scores in a review table. User can go back to edit or submit.

5. **Results**: Auto-saves to Supabase (creates game + all player results), then shows podium with gold/silver/bronze icons + full ranked list.

### Game History
- `MainTabsScreen` has a `Games` tab showing a scrollable `LazyColumn` of `GameListItem` cards
- Each item shows date + each player's name and total score
- Tap -> `GameDetailsScreen` (slide-up transition) showing per-player breakdown across all scoring categories
- Long-press -> `ConfirmationPopup` -> delete game
- Game details screen has a privacy toggle button and a delete button
- `GameDetailsViewModel` loads details via `gameRepository.getGameDetails(id)`

### Player Management
- `Players` tab shows a grid of players with stats: wins count, games played, top score, average placement
- FAB opens add-player popup (name + privacy toggle, with duplicate detection)
- Long-press on player -> edit popup (rename, toggle privacy, delete)
- Delete is soft-delete via `delete_player` RPC (sets `deleted = true`)
- Stats computed client-side in `PlayerRepositoryImpl.getPlayersWithStats()` by grouping all `PlayerResults` by `playerID`

### Admin Features
- `isAdmin()` RPC call checks `Roles` table
- Admin RLS bypass: admins see all games, players, and results regardless of privacy
- Admin RPC bypass: all 4 RPC functions check `is_admin()` as an alternative authorization

### Admin Panel (User Management)
- **Access**: Long-press FAB → Settings → "Admin Panel" button (only visible to admins)
- **Screen**: `AdminPanelScreen` navigates as a slide-up route from `NavHost`
- **Flow**: `MainTabsViewModel` checks `gameRepository.isAdmin()` on init → `SettingsPopup` conditionally shows the admin button → `AdminPanelScreen` lists all users from `auth.users` (via `list_users` RPC)
- **RPC `list_users()`**: `SECURITY DEFINER` function reading `auth.users` with a LEFT JOIN on `Roles`, returning all registered users. Users without a `Roles` entry get `role = 'user'` via `COALESCE`. Gated by `WHERE public.is_admin()`.
- **RPC `set_user_role()`**: `SECURITY DEFINER` function that upserts into `Roles`. Validates admin permission (`is_admin()`) and role value (`'user'` | `'admin'`).
- **RPC `delete_user_games(target_user_id)`**: `SECURITY DEFINER` function that deletes all `PlayerResults` and `Games` owned by the target user. Admin-only (`is_admin()` check).
- **RPC `delete_user_players(target_user_id)`**: `SECURITY DEFINER` function that soft-deletes all `Players` owned by the target user (sets `deleted = true`). Admin-only (`is_admin()` check).
- **RPC `delete_user_account(target_user_id)`**: `SECURITY DEFINER` function that deletes all `PlayerResults` → `Games` → `Players` → `Roles` → `auth.users` for the target user. Admin-only (`is_admin()` check).
- **UI per user row**: Avatar placeholder (person icon), display name, email, role badge (`"Admin"` in gold or `"User"` in gray), and a primary action button (`"Make Admin"` / `"Remove Admin"`). Below is a second row with **"Delete Games"**, **"Delete Players"**, and **"Delete Account"** buttons (all red, with confirmation popups). Each action shows a spinner while in flight. Current user marked with `"(you)"` and cannot perform actions on self.
- **Cross-screen sync**: After deleting games, navigating back signals `GameListScreen` to refresh via `savedStateHandle` (`"gamesDeleted"` key). After deleting players, navigating back signals `PlayerListScreen` to refresh via `savedStateHandle` (`"playersDeleted"` key). Uses the same pattern as `"gameAdded"` / `"gameDeleted"` for AddGame and GameDetails flows.

---

## Important Conventions & Gotchas

### List Mutation Helpers
Located at the bottom of `AddGameViewModel.kt`:
```kotlin
fun <T> MutableList<T>.push(item: T) { add(0, item) }       // prepend
fun <T> MutableList<T>.popOrNull(): T? { if (isEmpty()) null else removeAt(0) }  // dequeue from front
```
These implement a queue using the list as a deque -- `push` adds to front, `popOrNull` removes from front.

### Supabase Kotlin SDK Usage
- `supabaseClient.from("TableName").select().decodeList<T>()` for reads
- `supabaseClient.from("TableName").insert<T>(obj) { select() }.decodeSingle<T>()` for inserts (returns created object)
- `supabaseClient.postgrest.rpc("function_name", buildJsonObject { put("param", value) })` for RPC calls
- `serializer = KotlinXSerializer(Json { ignoreUnknownKeys = true })` in Postgrest plugin config

### Synchronization Pattern
`GameRepositoryImpl.getGames()` and `getGameDetails()` use `coroutineScope` with `async`/`await` to fetch related data in parallel:
- Games list: 3 parallel queries (Games + PlayerResults + Players) -> group in memory
- Game details: 3 parallel queries (PlayerResults filtered by gameID + single Game + all Players) -> join in memory

### Database Note
The file `database.db` at the project root is a local SQLite/Room database artifact. The app currently uses **only Supabase** as its database -- this file appears to be a leftover from a prior Room-based implementation and can be ignored.

### Authentication Architecture
Google Sign-In flow (not fully visible in this repository -- the Credential Manager integration and ID token retrieval happen in the UI layer / Activity result handling). The `AuthRepositoryImpl.signInWithGoogle()` receives an already-obtained ID token string and passes it to Supabase.

---

## Build Configuration

### App-level build.gradle.kts Key Details
- `applicationId`: `com.example.the7wcloud`
- `minSdk`: 26, `targetSdk`: 36, `compileSdk`: 36
- `useBuildConfig` enabled for `SUPABASE_URL`, `SUPABASE_ANON_KEY`, `WEB_CLIENT_ID`
- Plugins: `com.android.application`, `org.jetbrains.kotlin.android`, `org.jetbrains.kotlin.compose`, `org.jetbrains.kotlin.plugin.compose`, `com.google.dagger.hilt.android`, `com.google.devtools.ksp`, `org.jetbrains.kotlin.plugin.serialization`

### Key Dependencies (from libs.versions.toml)
- `androidx-compose-bom:2024.12.01`
- `androidx-navigation-compose:2.9.2`
- `androidx-lifecycle-runtime-compose:2.9.0`
- `dagger-hilt-android:2.57` + `hilt-compiler` (KSP)
- `supabase-kotlin:3.1.1` (bom), with `supabase-auth`, `supabase-postgrest-kt`, `supabase-serializer-json`
- `ktor-client-okhttp` for networking engine
- `kotlinx-serialization-json:1.9.0`

### Gradle
- Wrapper: `gradle-wrapper.properties` -> Gradle 8.13
- Catalog: `libs.versions.toml` centralizes all version numbers
- `settings.gradle.kts`: rootProject.name = `The7WCloud`, dependency resolution management for Supabase + Kotlin

---

## AndroidManifest

```xml
<uses-permission android:name="android.permission.INTERNET" />
<application android:theme="@style/Theme.The7WCloud" ...>
    <activity android:name=".MainActivity" ... />
</application>
```

---

## Testing

The project has minimal test scaffolding — placeholder unit and instrumentation tests exist but contain no meaningful assertions beyond verifying the application context loads.
