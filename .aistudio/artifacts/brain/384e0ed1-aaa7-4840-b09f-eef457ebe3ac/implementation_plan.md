# SpotCheck: Media Detective — Kids' Media Literacy App

An interactive, gamified Android application designed for pre-teens and middle schoolers (ages 9–13) that simulates a modern vertical video feed (Shorts/TikTok style) to teach kids how to spot misinformation, out-of-context video clips, emotional clickbait, and AI-generated media.

---

### User Review & Critical Decisions

> [!IMPORTANT]
> The following directions were confirmed during interactive clarification and shape this plan:
> - **Target Audience:** Pre-teens and middle schoolers (ages 9–13), featuring engaging, age-appropriate language, visual cues, and detective game mechanics.
> - **Simulation Style:** Realistic simulated social video feed with interactive playback, scrubber, comments, creator profiles, and an in-video **Clue Inspector** tool.
> - **Featured Topics:** Clickbait headlines & emotional exaggeration, out-of-context cuts & missing source context, and AI-generated / digitally manipulated media artifacts.

- **Confirmed Decision 1**: Pre-teen friendly detective persona ("Detective Byte") guiding the player through forensic video inspection using the established S.I.F.T. media literacy framework (Stop, Investigate source, Find better coverage, Trace original context).
- **Confirmed Decision 2**: Local Room persistence for detective progress, case history, unlocked badges, detective rank XP, and custom notes without requiring external accounts.
- **Recommended Default**: Dynamic simulated video player built with Compose Canvas, animated graphics, waveform audio, and simulated video timeline events so it runs smoothly on all devices without external video hosting dependencies.

---

### 1. Overview & Core Concept

**What It Does**:
Kids scroll through a simulated social video platform called **SpotCheck Feed**. Each video presents realistic everyday scenarios that young people encounter online: viral animal stunts, sensational science claims, influencer product miracles, dramatic breaking news, and uncanny AI deepfakes. Kids use detective tools to pause the video, inspect flagged visual and textual clues, read simulated comment threads, cross-examine sources, and issue an official **Fact Verdict**.

**Target Audience / Persona**:
Pre-teens and middle schoolers (ages 9–13), digital natives who consume short-form video but may lack the critical skepticism to differentiate between authentic journalism, clever satire, emotionally manipulative clickbait, and synthetic AI.

**Key Value & Learning Outcomes**:
- **Critical Questioning**: Cultivate reflex to ask "Who made this?", "What is the evidence?", and "What was cut out?"
- **Hands-on Forensics**: Identify tangible clues: exaggerated shock words, synthetic voice cadence, uncanny physical glitches, cropped watermarks, and missing author credentials.
- **Immediate Rewarding Feedback**: Earn detective XP, unlock badges (e.g., *Deepfake Decoder*, *Context Crusader*, *Clickbait Buster*), and build an instinct for healthy skepticism online.

---

### 2. User Experience & Visual Design

#### Key User Flows
1. **The Investigation Feed (Core Flow)**:
   - User browses the vertical video feed with fluid vertical paging.
   - Each post displays creator handle, verification status, follower count, video title, animated video presentation with playback controls, and a prominent **"Inspect Clues"** floating action.
2. **Interactive Clue Hunting (Investigation Mode)**:
   - Tapping "Inspect Clues" pauses the clip and overlays a forensic HUD with interactive hotspots on the video frame (e.g., tapping a weird hand with 6 fingers, or tapping a dramatic headline).
   - A bottom drawer allows kids to inspect 3 tabs:
     - **Headline & Tone**: Analyzes emotional triggers and clickbait red flags.
     - **Source & Author**: Checks creator background, funding, and credentials.
     - **Evidence & Context**: Compares the snippet to the original full-length event.
3. **Verdict & Case Debrief**:
   - The user selects their final verdict: **Verified Authentic**, **Misleading / Out of Context**, **AI / Digitally Manipulated**, or **Clickbait Exaggeration**.
   - Immediate animated reveal: explains the true story, breaks down every trick used, and awards Detective XP and skill badges.
4. **Detective Field Guide & Academy**:
   - A reference handbook teaching real-world media literacy techniques: the S.I.F.T. method, lateral reading, spot-the-bot tips, and an AI deepfake checklist.
5. **Detective Profile & Trophy Room**:
   - Displays solved cases, accuracy rate, earned badges, and rank progression (from *Novice Sleuth* to *Master Fact-Finder*).

#### Visual Identity & Theme
- **Aesthetic Direction**: *Tech-Detective Lab meets Gen-Z Social App*. Vibrant, high-contrast, playful yet authoritative.
- **Color Palette**:
  - `Primary`: Cyber Electric Violet (`#7C3AED`) — dynamic, inquisitive, modern.
  - `Secondary / Accent`: Neon Cyan / Mint (`#06B6D4` / `#10B981`) — detective analysis and verified truth.
  - `Warning / Misinformation`: Amber / Coral Red (`#F59E0B` / `#EF4444`) — alerts and identified red flags.
  - `Background`: Deep Midnight Slate (`#0F172A`) for the simulated video feed, with crisp high-readability cards (`#1E293B`).
- **Typography & Hierarchy**:
  - Bold, friendly headlines with high legible geometry for pre-teens.
  - Clear pill tags for categories: `[AI Alert]`, `[Out of Context]`, `[Clickbait]`, `[Verified Source]`.
- **Component Styling & Motion**:
  - Snappy vertical pager transitions mimicking modern social video apps.
  - Pulsing radar circles on suspect video areas during Clue Inspection mode.
  - Confetti and badge celebration animations on correct verdicts.

---

### 3. Key Product Decisions & Trade-Offs

- **Decision 1: Self-Contained Simulated Media Engine vs External Video Streaming**:
  - *Chosen Approach*: Build custom interactive Compose canvas animations and rich frame states depicting video scenes (with scrubbable timelines, playback state, visual glitch markers, and soundwave graphics).
  - *Why*: Ensures 100% offline reliability, zero external CDN latency, and precise control over forensic hotspot locations (e.g. highlighting a 6-fingered hand or mismatched lips at timestamp 0:04).
- **Decision 2: Local Room Persistence**:
  - *Chosen Approach*: Store case records, kid progress, solved cases, and badge achievements locally via Room Database.
  - *Why*: Privacy-first for kids, instant launch without login barriers, and completely persistent across app sessions.
- **Decision 3: Pedagogical Structure (The S.I.F.T. Method)**:
  - *Chosen Approach*: Embed the internationally recognized S.I.F.T. framework directly into the inspection UI so habits translate directly to real-world internet browsing.

---

### 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────┐
│                      UI LAYER                          │
│  ┌───────────────────────┐  ┌───────────────────────┐  │
│  │   FeedScreen          │  │ ClueInspectionSheet   │  │
│  │ (Vertical Pager,      │  │ (Hotspots, Source     │  │
│  │  Simulated Video Player│  │  Checker, Comments)   │  │
│  └───────────┬───────────┘  └───────────┬───────────┘  │
│              │                          │              │
│  ┌───────────▼───────────┐  ┌───────────▼───────────┐  │
│  │    VerdictDialog      │  │   FieldGuideScreen    │  │
│  │  (XP, Truth Reveal,   │  │  (S.I.F.T. Guide,     │  │
│  │   Badges Earned)      │  │   Detective Profile)  │  │
│  └───────────┬───────────┘  └───────────┬───────────┘  │
└──────────────┼──────────────────────────┼──────────────┘
               ▼                          ▼
┌────────────────────────────────────────────────────────┐
│                   VIEWMODEL LAYER                      │
│             SpotCheckViewModel (StateFlow)             │
│   - currentVideoIndex, isInspecting, activeHotspot     │
│   - userVerdict, solvedCases, detectiveStats           │
└──────────────────────────────┬─────────────────────────┘
                               ▼
┌────────────────────────────────────────────────────────┐
│                   REPOSITORY LAYER                     │
│               MediaDetectiveRepository                 │
│   - Bundled Scenarios (AI, Out-of-Context, Clickbait)  │
│   - Room DAO for Solved Cases & Earned Badges          │
└──────────────────────────────┬─────────────────────────┘
                               ▼
┌────────────────────────────────────────────────────────┐
│                  DATA PERSISTENCE                      │
│              Room Database: SpotCheckDb                │
│   - SolvedCaseEntity (caseId, verdict, timestamp, score)│
│   - BadgeEntity (badgeId, title, unlockedAt)           │
│   - UserProfileEntity (xp, rank, streak)               │
└──────────────────────────────┘
```

#### Data Model Entities
- **VideoCase**:
  - `id`: Unique identifier (e.g., `"case_ai_cat"`, `"case_climate_cut"`, `"case_miracle_soda"`).
  - `title`: Video headline (e.g., *"SHOCKING: Giant Mutant Dog Discovered in Backyard!"*).
  - `creator`: Handle, avatar color/icon, follower count, verified status.
  - `category`: `CLICKBAIT`, `OUT_OF_CONTEXT`, `AI_GENERATED`, or `CREDIBLE`.
  - `videoTheme`: Color scheme, visual scene composition, simulated duration.
  - `clues`: List of detectable clues (type, timestamp, description, hint, rect bounds).
  - `fullStory`: The real background story explaining the manipulation or truth.
  - `sourceFacts`: Who actually made the original footage and when.
  - `comments`: Simulated realistic comments (skeptical, gullible, bot-like).

#### Interactive State Mapping
- **Swipe Between Videos**: Updates `activeCaseId`, resets inspection hotspots and scrubber.
- **Tap "Inspect Clues"**: Pauses video, opens forensic overlay, displays radar hotspots.
- **Tap Video Hotspot**: Triggers detective sound effect/haptic and highlights explanation card.
- **Submit Verdict**: Validates against ground truth, calculates score based on discovered clues, saves to Room DB, triggers badge unlock check, and displays animated debrief sheet.
