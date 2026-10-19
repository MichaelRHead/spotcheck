# Google Play Store Listing & Asset Guide — SpotCheck

All required graphic and text assets for publishing **SpotCheck** to the Google Play Store are located in this `/play_store_assets` directory.

---

## 1. Graphic Assets Included (`play_store_assets/`)

| Asset Type | File Name | Dimensions | Format | Play Console Requirement |
| :--- | :--- | :--- | :--- | :--- |
| **App Icon** | `ic_launcher_playstore_512x512.png` | `512 x 512 px` | 32-bit PNG | High-res icon (Max 1024 KB) |
| **Feature Graphic** | `feature_graphic_1024x500.png` | `1024 x 500 px` | 24-bit PNG (no alpha) | Required for store listing header |
| **Phone Screenshot 1** | `screenshot_1_feed_1080x1920.png` | `1080 x 1920 px` | PNG (9:16) | Forensic video feed & clue inspection |
| **Phone Screenshot 2** | `screenshot_2_verdict_1080x1920.png` | `1080 x 1920 px` | PNG (9:16) | Case verdict & S.I.F.T. breakdown |
| **Phone Screenshot 3** | `screenshot_3_field_guide_1080x1920.png` | `1080 x 1920 px` | PNG (9:16) | S.I.F.T. Field Guide & Red Flag Encyclopedia |
| **Phone Screenshot 4** | `screenshot_4_dossier_lab_1080x1920.png` | `1080 x 1920 px` | PNG (9:16) | Detective Dossier, Badges & Clickbait Lab |
| **7-inch Tablet Screenshot** | `tablet_7inch_1920x1080.png` | `1920 x 1080 px` | PNG (16:9) | Tablet view of Feed & Forensic Inspector |
| **10-inch Tablet Screenshot** | `tablet_10inch_1920x1080.png` | `1920 x 1080 px` | PNG (16:9) | Expanded split-screen view for 10" tablets |

---

## 2. Store Listing Copy (Copy & Paste into Google Play Console)

### App Name (Max 30 characters)
```text
SpotCheck: Media Detective
```
*(26 characters — compliant with Google Play's 30-character policy and free of promotional buzzwords or emojis)*

### Short Description (Max 80 characters)
```text
Learn media literacy! Spot clickbait, bias, and AI fakes in a fun video feed.
```
*(77 characters)*

### Full Description (Max 4000 characters)
```text
Can you spot the fake before sharing? Welcome to SpotCheck: Media Detective—an interactive media literacy adventure designed for pre-teens, middle schoolers (ages 9–13), parents, and classrooms!

Social video feeds move fast, and it has never been easier to get tricked by sensational clickbait, out-of-context video cuts, hidden sponsorships, or AI-generated illusions. SpotCheck puts young digital citizens in the role of a Media Detective with a safe, offline, simulated social video feed where every video is a mystery waiting to be solved.

HOW TO PLAY & INVESTIGATE
• Scroll the Simulated Video Feed: Watch engaging, animated short-form video scenarios modeled after real-world viral trends—with zero external links, zero tracking, and zero real social media hazards.
• Activate the Forensic Lens: Pause any video and turn on the Detective Magnifying Glass to reveal interactive hotspots directly on the video frame and headline.
• Read Laterally in the Comments: Open the simulated comments drawer to separate skeptical fact-checkers from automated hype bots.
• Run a Source Check: Inspect creator bios, account history, verification status, and original upload dates using the S.I.F.T. framework.
• Submit Your Verdict: Classify each case as Credible & Verified, Clickbait / Exaggerated, Out-of-Context Clip, AI-Generated / Manipulated, or Sponsored Bias. Receive immediate, encouraging feedback and earn Detective XP!

KEY LEARNING FEATURES
• The S.I.F.T. Field Guide: Master the four gold-standard pillars of digital verification—Stop, Investigate the Source, Find Better Coverage, and Trace Claims to the Original Context.
• Red Flag Encyclopedia: Explore visual checklists for spotting AI video glitches (warped paws, extra fingers, garbled background text), emotional rage-bait headlines, and selective jump-cuts.
• Interactive Clickbait Headline Lab: Experiment in the sandbox by swapping sensational alarm words into neutral news stories to see firsthand how clickbait manipulates curiosity and emotion.
• Detective Dossier & Badges: Track accuracy rates, maintain investigation streaks, level up from Junior Fact Finder to Chief Truth Officer, and unlock 6 specialized detective badges.

BUILT FOR SAFETY & PRIVACY
• 100% Offline & Self-Contained: All video scenarios, comments, and forensic clues are bundled directly inside the app.
• No Data Collection: SpotCheck does not collect personal information, require an account, access the camera/microphone, or display third-party advertisements.
```

---

## 3. Release Notes (Version 2.0 / Version Code 2)

```text
<en-US>
• Added interactive Fact-Check Service with detailed reliability scores and red-flag explanations.
• Added Detective Badge progression tracker and milestone rewards.
• Added S.I.F.T. Field Guide and interactive Clickbait Headline Sandbox.
• Updated to Version 2.0 (versionCode 2).
</en-US>
```

---

## 4. Play Console Categorization & Policy Declarations

* **App Category:** Education
* **Tags:** Media Literacy, Critical Thinking, Educational Game, Kids, Fact Checking
* **Target Audience & Content:** Ages 9–12, 13–15 (Pre-teens & Middle Schoolers)
* **Ads Declaration:** No, my app does not contain ads.
* **Data Safety Section:** No data collected or shared. All progress and badges are stored strictly locally on the device via Android Room SQLite database.
