# Electricity Bill Calculator
### O3 Interfaces — Android Assessment

**Developer:** U5M4N  
**Platform:** Android (Kotlin)  
**Architecture:** MVI + Clean Architecture  
**Minimum SDK:** 26 (Android 8.0) | **Target SDK:** 35 (Android 15)

---

## Table of Contents

1. [Overview](#overview)
2. [Requirements Coverage](#requirements-coverage)
3. [How to Configure the Slab Table](#how-to-configure-the-slab-table)
4. [Architecture](#architecture)
5. [Project Structure](#project-structure)
6. [Tech Stack](#tech-stack)
7. [Algorithm — Progressive Slab Calculation](#algorithm--progressive-slab-calculation)
8. [Validation Rules](#validation-rules)
9. [How to Build and Run](#how-to-build-and-run)
10. [Test Scenarios](#test-scenarios)
11. [Code Quality](#code-quality)

---

## Overview

The Electricity Bill Calculator is a fully offline Android application that calculates electricity consumption costs for households using a configurable progressive slab pricing model. A customer enters their **Service Number** and **current meter reading**; the app fetches any previous reading for that customer, calculates the consumption delta, applies the slab rates, and displays a full cost breakdown. The result can be saved to a local SQLite database, which also powers the historical readings table shown on the result screen.

---

## Requirements Coverage

| # | Requirement | Status | Implementation |
|---|---|---|---|
| 1a | Service Number text box — 10-digit alphanumeric, mandatory | ✅ | `InputForm.kt` + `ValidateInputUseCase.kt` |
| 1a-i | Validation message for incorrect service number | ✅ | Inline `supportingText` on `OutlinedTextField` |
| 1b | Current meter reading — positive numeric values only | ✅ | `KeyboardType.Number` + regex `[0-9]+` |
| 1b-i | Validation message for incorrect meter reading | ✅ | Inline `supportingText` on `OutlinedTextField` |
| 1c | Submit button | ✅ | `InputForm.kt` — disabled during loading |
| 2a-i | Show last 3 historical readings in a table | ✅ | `HistoryTable.kt` — Room LIMIT 3 query |
| 2a-ii | Calculate difference: current reading − previous reading | ✅ | `MeterReadingViewModel.handleSubmit()` |
| 2a-iii | Show consumption cost | ✅ | `BillResultCard.kt` |
| 2b-i | New customer: calculate using full reading value | ✅ | `MeterReadingViewModel` — no previous record found |
| 2b-ii | Show consumption cost for new customer | ✅ | `BillResultCard.kt` |
| 3 | Save button — stores reading + cost, returns to entry | ✅ | `handleSave()` → Room insert → `MeterReadingUiState()` reset |
| 4a | Warning when reading is less than previous | ✅ | `ValidateInputUseCase` — field-level error message |
| 4b | Warning when reading is a negative value | ✅ | `ValidateInputUseCase` — regex + guard check |
| 5 | Configurable slab table (no UI required) | ✅ | `assets/slab_config.json` — edit before running |

---

## How to Configure the Slab Table

The slab pricing table is defined in:

```
app/src/main/assets/slab_config.json
```

**Format:**

```json
{
  "slabs": [
    { "upToUnits": 100,  "ratePerUnit": 5.0  },
    { "upToUnits": 500,  "ratePerUnit": 8.0  },
    { "upToUnits": null, "ratePerUnit": 10.0 }
  ]
}
```

**Rules:**

| Field | Type | Description |
|---|---|---|
| `upToUnits` | `Int` or `null` | The inclusive upper bound of this slab in units. Set to `null` for the final (unlimited) tier. |
| `ratePerUnit` | `Double` | The cost per unit for consumption falling within this slab. |

**Important:**
- Slabs must be ordered from lowest to highest unit bound.
- Exactly **one** slab must have `"upToUnits": null` and it must be the **last** entry — this captures all consumption beyond the highest defined bound.
- You can define **any number of slabs**.
- Edit this file before building/running the app. No code changes are needed.

**Example — 2-slab configuration:**

```json
{
  "slabs": [
    { "upToUnits": 200,  "ratePerUnit": 4.0  },
    { "upToUnits": null, "ratePerUnit": 9.0  }
  ]
}
```

---

## Architecture

The app follows **MVI (Model-View-Intent)** pattern layered on top of **Clean Architecture** with three distinct layers:

```
┌─────────────────────────────────────────┐
│           Presentation Layer            │
│  MeterReadingScreen (Compose UI)        │
│  MeterReadingViewModel (MVI ViewModel)  │
│  Intent / UiState / SideEffect          │
├─────────────────────────────────────────┤
│             Domain Layer                │
│  Use Cases (business logic)             │
│  Repository Interfaces (contracts)      │
│  Domain Models                          │
├─────────────────────────────────────────┤
│              Data Layer                 │
│  Room (local database)                  │
│  SlabConfigDataSource (assets JSON)     │
│  Repository Implementations             │
│  DTOs + Mappers                         │
└─────────────────────────────────────────┘
```

### MVI Data Flow

```
User Action
    │
    ▼
MeterReadingIntent
    │
    ▼
MeterReadingViewModel.processIntent()
    │
    ├── validates input (ValidateInputUseCase)
    ├── fetches history + slabs in parallel (async/await)
    ├── re-validates with previous reading
    ├── calculates bill (CalculateElectricityBillUseCase)
    │
    ▼
StateFlow<MeterReadingUiState>  ──►  Compose UI re-renders
    +
Channel<MeterReadingSideEffect>  ──►  Snackbar / navigation
```

---

## Project Structure

```
app/src/main/java/com/o3interfaces/electricitybill/
│
├── ElectricityBillApplication.kt       # Hilt app class + Timber init
├── MainActivity.kt                      # Single Activity host
│
├── di/
│   ├── AppModule.kt                     # Provides Gson singleton
│   ├── DatabaseModule.kt               # Provides Room DB + DAO
│   └── RepositoryModule.kt             # Binds interfaces to impls
│
├── data/
│   ├── dto/
│   │   └── SlabConfigDto.kt            # Gson DTOs for slab_config.json
│   ├── local/
│   │   ├── dao/ReadingRecordDao.kt     # Room DAO (queries + insert)
│   │   ├── db/AppDatabase.kt           # Room database definition
│   │   ├── entity/ReadingRecordEntity.kt  # Room table entity
│   │   └── mapper/ReadingRecordMapper.kt  # Entity ↔ Domain mapping
│   ├── repository/
│   │   ├── MeterReadingRepositoryImpl.kt  # Meter reading persistence
│   │   └── SlabConfigRepositoryImpl.kt    # Slab config loading
│   └── source/
│       └── SlabConfigDataSource.kt     # Reads assets/slab_config.json
│
├── domain/
│   ├── model/
│   │   ├── BillCalculation.kt          # Full bill result model
│   │   ├── ReadingRecord.kt            # Saved reading model
│   │   ├── Resource.kt                 # Loading / Success / Error wrapper
│   │   ├── SlabBreakdown.kt            # Per-slab cost breakdown model
│   │   └── SlabConfig.kt              # Single pricing slab model
│   ├── repository/
│   │   ├── MeterReadingRepository.kt   # Persistence contract
│   │   └── SlabConfigRepository.kt     # Slab config contract
│   └── usecase/
│       ├── CalculateElectricityBillUseCase.kt  # Core billing algorithm
│       ├── GetHistoricalReadingsUseCase.kt      # Fetch last 3 readings
│       ├── GetSlabConfigUseCase.kt              # Fetch slab config
│       ├── SaveReadingUseCase.kt                # Persist a reading
│       └── ValidateInputUseCase.kt              # Input validation
│
└── presentation/
    ├── theme/
    │   ├── Color.kt                    # Brand color palette
    │   ├── Theme.kt                    # Material3 theme composable
    │   └── Type.kt                     # Typography definitions
    └── screen/
        ├── MeterReadingIntent.kt       # MVI intents (user actions)
        ├── MeterReadingSideEffect.kt   # One-shot effects (snackbar)
        ├── MeterReadingUiState.kt      # Immutable UI state
        ├── MeterReadingScreen.kt       # Root screen composable
        ├── MeterReadingViewModel.kt    # MVI ViewModel
        └── components/
            ├── BillResultCard.kt       # Bill breakdown card UI
            ├── HistoryTable.kt         # Historical readings table UI
            └── InputForm.kt            # Entry form UI

app/src/main/assets/
    └── slab_config.json                # ← Configure slab pricing here
```

---

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Kotlin | 2.0.21 | Primary language |
| Android Gradle Plugin | 9.1.1 | Build system |
| Jetpack Compose | BOM 2024.09.03 | Declarative UI |
| Material3 | 1.3.1 | Design system |
| Hilt | 2.59.2 | Dependency injection |
| Room | 2.7.0 | Local SQLite database |
| Kotlin Coroutines | 1.8.1 | Async operations |
| Gson | 2.11.0 | JSON parsing for slab config |
| Timber | 5.0.1 | Structured logging |

---

## Algorithm — Progressive Slab Calculation

The billing algorithm is implemented in `CalculateElectricityBillUseCase.kt` with **O(n)** time complexity where n is the number of configured slabs.

### How it works

1. Start with the full consumption in `remainingUnits`.
2. Iterate through slabs in order from lowest to highest bound.
3. For each slab, determine its **capacity** = `upToUnits − previousUpperBound`.
4. Consume `min(remainingUnits, slabCapacity)` units from this slab.
5. Record the sub-total for this slab as a `SlabBreakdown`.
6. Subtract the consumed units from `remainingUnits` and advance `previousUpperBound`.
7. Stop when `remainingUnits` reaches 0 or all slabs are exhausted.

### Worked examples using the default slab config

**Slab config:** ≤100 @ $5/unit | 101–500 @ $8/unit | >500 @ $10/unit

| Scenario | Consumption | Breakdown | Total |
|---|---|---|---|
| New customer, 250 units | 250 | 100×$5 + 150×$8 | **$1,700** |
| New customer, 510 units | 510 | 100×$5 + 400×$8 + 10×$10 | **$3,800** |
| Existing customer, prev=510, curr=630 | 120 | 100×$5 + 20×$8 | **$660** |
| Slab boundary, 100 units | 100 | 100×$5 | **$500** |
| Zero consumption | 0 | — | **$0.00** |

---

## Validation Rules

All validation is handled by `ValidateInputUseCase.kt` and errors are shown inline beneath each text field.

### Service Number

| Rule | Error Message |
|---|---|
| Empty / blank | "Service number is required" |
| Not exactly 10 characters | "Service number must be 10 characters" |
| Contains non-alphanumeric characters | "Service number must be alphanumeric only" |

### Meter Reading

| Rule | Error Message |
|---|---|
| Empty / blank | "Meter reading is required" |
| Contains non-digit characters | "Meter reading must be a positive number" |
| Negative value | "Meter reading cannot be negative" |
| Less than the last saved reading | "Reading (X) cannot be less than last recorded reading (Y)" |

The "less than previous" check is performed **after** fetching history, so the exact previous reading value is shown in the error message.

---

## How to Build and Run

### Prerequisites

- Android Studio Meerkat or later
- JDK 17+
- Android device or emulator (API 26+)

### Steps

1. Clone or open the project in Android Studio.
2. *(Optional)* Edit `app/src/main/assets/slab_config.json` to configure your slab pricing before running.
3. Let Gradle sync complete.
4. Click **Run ▶** or execute from terminal:

```bash
./gradlew assembleDebug
```

5. Install the APK on a connected device:

```bash
./gradlew installDebug
```

---

## Test Scenarios

Use these scenarios to verify correct behaviour after configuring the default slab table:

```
Slab config: ≤100 @ $5 | 101–500 @ $8 | >500 @ $10
```

| # | Service Number | Reading | Expected Total | Notes |
|---|---|---|---|---|
| 1 | HA12345678 | 250 | $1,700 | New customer — 2 slab rows |
| 2 | HA45678901 | 510 | $3,800 | New customer — 3 slab rows |
| 3 | HA45678901 | 630 | $660 | Delta: 630−510=120 units |
| 4 | HA99999999 | 100 | $500 | Exact slab boundary — 1 row |
| 5 | HA99999999 | 0 | $0.00 | Zero consumption |
| 6 | AB12345 | 100 | Validation error | Service number too short |
| 7 | AB@123!!!! | 100 | Validation error | Special characters in service number |
| 8 | HA45678901 | 400 | Validation error | Reading less than previous (630) |

---

## Code Quality

| Aspect | Implementation |
|---|---|
| **Exception handling** | All repository methods wrap operations in `try/catch`, returning `Resource.Error` with the exception message |
| **Logging** | Timber with `DebugTree` in debug builds; slab load logged at `WARN`, save at `INFO`, errors at `ERROR`, validation failures at `DEBUG` |
| **Validation** | Dedicated `ValidateInputUseCase` with typed `ValidationResult` sealed class; all rules clearly separated |
| **Threading** | All IO on `Dispatchers.IO`, algorithm on `Dispatchers.Default`, UI on `Main` via `viewModelScope` |
| **Parallel execution** | History fetch and slab config fetch run concurrently via `async/await` in `handleSubmit()` |
| **Immutable state** | `MeterReadingUiState` is a `data class` updated via `copy()` — no mutable shared state |
| **Dependency inversion** | Domain layer has zero dependency on data layer; repositories are injected as interfaces |
| **DI** | Hilt throughout — no manual singletons or static state |
