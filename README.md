# 🔍 SpotCheck: Media Detective

**SpotCheck** is an interactive, gamified Android application designed for pre-teens and middle schoolers (ages 9–13) to build real-world media literacy skills. Using a simulated short-form social video feed, kids act as digital detectives—pausing clips, scanning for forensic clues, checking sources, analyzing comment sections, and issuing official fact-check verdicts on viral videos.

---

## ✨ Key Features

### 📱 1. Simulated Social Video Feed (`FeedScreen`)
- **Self-Contained Animated Video Engine**: Custom Jetpack Compose Canvas scenes simulating viral short-form videos (AI mutant pets, out-of-context storm footage, miracle study drink ads, verified NASA space discoveries, staged rescues, and deepfake speeches) with scrubbable timelines and playback controls.
- **Forensic Clue Radar (`ClueDetailSheet`)**: Toggling **Inspect** pauses playback and overlays interactive radar beacons on suspicious visual, audio, and textual anomalies (e.g., extra claws on an AI-generated cat, a 2011 movie marquee in "today's" storm footage, or lip-sync lag).
- **Community Comments Drawer (`CommentsDrawer`)**: Simulated viewer comments teaching kids how to spot skeptical community fact-checkers versus engagement bots.
- **Source & Origin Dossier (`SourceCheckSheet`)**: A lateral reading toolkit that reveals account creation dates, original upload sources, and step-by-step guidance using the **S.I.F.T.** methodology.

### ⚖️ 2. Fact-Checking Engine & Debrief (`FactCheckService` & `VerdictModal`)
- **Four Official Verdicts**:
  - 🟢 **Verified Authentic** (Credible journalism & primary sources)
  - 🟡 **Misleading / Out of Context** (Real footage repurposed with false dates or selective cuts)
  - 🟣 **AI Generated / Manipulated** (Deepfakes, voice clones, and generative visual glitches)
  - 🔴 **Clickbait Exaggeration** (Sensational emotional triggers or disguised sales pitches)
- **Comprehensive `FactCheckResult` Feedback**: Calculates a **0–100 Content Reliability Score**, lists specific **Detected Red Flags** and **Credibility Indicators**, assigns an **Assessment Grade** (*Master Sleuth*, *Sharp Eye*, *Close Call*, or *Fooled by the Trap*), and explains the real story behind each clip.

### 📚 3. Detective Academy Field Guide (`FieldGuideScreen`)
- **The S.I.F.T. Method**: Interactive reference for **S**top, **I**nvestigate the source, **F**ind better coverage, and **T**race claims to the original context.
- **AI & Deepfake Checklist**: Practical tips for spotting anatomical glitches, unnatural blinking, shadow physics errors, and synthetic voiceovers.
- **Clickbait Trap Lexicon**: Highlights common emotional manipulation phrases (*"Share before deleted!"*, *"Doctors hate this trick"*).

### 🧪 4. The Misinformation Lab Sandbox (`SandboxScreen`)
- An interactive experiment where kids step into the shoes of a content editor—toggling clickbait headlines, removing source citations, and adding AI glitches to see how the real-time **Misinformation Threat Score** changes.

### 🏆 5. Detective Dossier & Gamified Progression (`ProgressTrackerService` & `ProfileScreen`)
- **Tiered Badges (`Badge`)**: Unlockable Bronze, Silver, Gold, and Diamond badges across categories (*First Case Closed*, *Deepfake Decoder*, *AI Forensics Prodigy*, *Context Champion*, *Clickbait Buster*, *Source Sleuth*, *Clue Master*, *Eagle Eye Sleuth*, *Fact-Check Streak*, and *Grand Master Investigator*) with live progress bars.
- **Rank Progression & Streaks**: Earn Detective XP and streak bonuses to advance from *Rookie Sleuth* to *Chief Forensic Fact-Checker*, persisted locally on-device with Room.

---

## 🏗️ Architecture & Tech Stack

- **Language**: 100% Kotlin
- **UI Framework**: Jetpack Compose (Material Design 3, Custom Canvas Animations, Edge-to-Edge WindowInsets)
- **Architecture**: MVVM + Clean Domain Services
  - **UI Layer**: `FeedScreen`, `FieldGuideScreen`, `SandboxScreen`, `ProfileScreen`, `SpotCheckViewModel`
  - **Domain Layer**:
    - `FactCheckService` (`MediaFactCheckService`) — Evaluates user verdicts, computes content reliability scores, and extracts red flags/credibility signals.
    - `ProgressTrackerService` (`MediaProgressTrackerService`) — Tracks streaks, calculates detective ranks, and evaluates multi-tiered badge progress.
  - **Data Layer**: `MediaDetectiveRepository`, `ScenariosData`, and **Room Database** (`SpotCheckDatabase`, `SpotCheckDao`, `SolvedCaseEntity`, `UserStatsEntity`, `BadgeEntity`) for privacy-first offline persistence.
- **Testing**: JUnit 4 & Robolectric (`FactCheckServiceTest`, `ProgressTrackerServiceTest`, `ExampleRobolectricTest`).

---

## 📂 Project Structure

```text
app/src/main/java/com/example/
├── MainActivity.kt                  # App entry point & bottom navigation host
├── data/
│   ├── local/                       # Room Database, DAO, and Entities
│   │   ├── SpotCheckDatabase.kt
│   │   ├── SpotCheckDao.kt
│   │   └── SpotCheckEntities.kt
│   ├── model/                       # Core domain & UI data models
│   │   ├── Badge.kt
│   │   ├── CaseVerdict.kt
│   │   ├── DetectionClue.kt
│   │   ├── FactCheckResult.kt
│   │   └── MediaCase.kt
│   └── repository/                  # Bundled scenarios & data repository
│       ├── MediaDetectiveRepository.kt
│       └── ScenariosData.kt
├── domain/                          # Business logic & evaluation services
│   ├── FactCheckService.kt
│   └── ProgressTrackerService.kt
└── ui/
    ├── components/                  # Reusable Compose UI components & modals
    │   ├── ClueDetailSheet.kt
    │   ├── CommentsDrawer.kt
    │   ├── SimulatedVideoPlayer.kt
    │   ├── SourceCheckSheet.kt
    │   └── VerdictModal.kt
    ├── screens/                     # Top-level destinations
    │   ├── FeedScreen.kt
    │   ├── FieldGuideScreen.kt
    │   ├── ProfileScreen.kt
    │   └── SandboxScreen.kt
    ├── theme/                       # Material 3 cyber-detective color scheme & typography
    └── viewmodel/
        └── SpotCheckViewModel.kt    # Reactive UI state management
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio** Ladybug or newer
- **JDK 11+**
- **Android SDK 36** (Min SDK 24)

### Build & Run
1. Clone the repository:
   ```bash
   git clone <your-repo-url>
   cd spotcheck
   ```
2. Open the project in Android Studio and let Gradle sync, or assemble the debug APK from the command line:
   ```bash
   ./gradlew assembleDebug
   ```

### Running Tests
Run the unit and Robolectric test suite covering the fact-check engine, badge progression service, and scenario repository:
```bash
./gradlew :app:testDebugUnitTest
```
