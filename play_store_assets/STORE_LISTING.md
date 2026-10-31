# Google Play Store Listing & Asset Guide — SpotCheck

All required graphic and text assets for publishing **SpotCheck** to the Google Play Store (including Phone, 7" & 10" Tablets, and Google TV) are located in this `/play_store_assets` directory and mirrored in `/fastlane/metadata/android/en-US/`.

---

## 1. Graphic Assets Included (`play_store_assets/` & `fastlane/`)

All screenshots, feature graphics, and Google TV banners have been updated to showcase the **photorealistic forensic video scenes**, **2.6x optical zoom loupe**, and **solved case archive thumbnails**.

| Asset Type | File Name | Dimensions | Format | Play Console Requirement |
| :--- | :--- | :--- | :--- | :--- |
| **App Icon** | `ic_launcher_playstore_512x512.png` | `512 x 512 px` | 32-bit PNG | High-res icon (Max 1024 KB) |
| **Feature Graphic** | `feature_graphic_1024x500.png` | `1024 x 500 px` | 24-bit PNG (no alpha) | Required for store listing header (`fastlane/.../featureGraphic.png`) |
| **Phone Screenshot 1** | `screenshot_1_feed_1080x1920.png` | `1080 x 1920 px` | PNG (9:16) | Photorealistic video feed & 2.6x optical zoom clue inspection |
| **Phone Screenshot 2** | `screenshot_2_verdict_1080x1920.png` | `1080 x 1920 px` | PNG (9:16) | Case verdict, photorealistic evidence log & S.I.F.T. breakdown |
| **Phone Screenshot 3** | `screenshot_3_field_guide_1080x1920.png` | `1080 x 1920 px` | PNG (9:16) | S.I.F.T. Field Guide with real-world photographic case examples |
| **Phone Screenshot 4** | `screenshot_4_dossier_lab_1080x1920.png` | `1080 x 1920 px` | PNG (9:16) | Detective Dossier, Solved Case Archive, Badges & Clickbait Lab |
| **7-inch Tablet Screenshot** | `tablet_7inch_1920x1080.png` | `1920 x 1080 px` | PNG (16:9) | Tablet split view of Photorealistic Feed & 2.6x Optical Loupe |
| **10-inch Tablet Screenshot** | `tablet_10inch_1920x1080.png` | `1920 x 1080 px` | PNG (16:9) | Expanded 10" split-screen Deepfake Podium inspection & Case Queue |
| **Android TV Banner (Standard)** | `tv_banner_1280x720.png` | `1280 x 720 px` | 24-bit PNG (no alpha) | Required Play Console TV Banner (`fastlane/.../tvBanner.png`) |
| **Android TV Banner (Full HD)** | `tv_banner_fhd_1920x1080.png` | `1920 x 1080 px` | 24-bit PNG (no alpha) | High-res 1080p 16:9 Primary Brand TV Banner |
| **Android TV Banner (4K UHD)** | `tv_banner_uhd_3840x2160.png` | `3840 x 2160 px` | 24-bit PNG (no alpha) | Ultra-HD 4K 16:9 Promotional TV Hero Banner |
| **Android TV Banner (Forensic Lab)** | `tv_banner_forensic_lab_1920x1080.png` | `1920 x 1080 px` | 24-bit PNG (no alpha) | Alternate TV Banner: Photorealistic Deepfake & AI Artifact Inspector |
| **Android TV Banner (Couch Co-Op)** | `tv_banner_couch_coop_1920x1080.png` | `1920 x 1080 px` | 24-bit PNG (no alpha) | Alternate TV Banner: Living Room & Classroom S.I.F.T. Edition |
| **Android TV Screenshot 1** | `tv_screenshot_1_feed_1920x1080.png` | `1920 x 1080 px` | 24-bit PNG (16:9) | 10-foot TV UI: Navigation Rail, Photorealistic Feed & 2.6x TV Loupe |
| **Android TV Screenshot 2** | `tv_screenshot_2_field_guide_1920x1080.png` | `1920 x 1080 px` | 24-bit PNG (16:9) | 10-foot TV UI: Clickbait Lab & Solved Photorealistic Cases |

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
Can you spot the fake before sharing? Welcome to SpotCheck: Media Detective—an interactive media literacy adventure designed for pre-teens, middle schoolers (ages 9–13), parents, and classrooms on Phone, Tablet, and Google TV!

Social video feeds move fast, and it has never been easier to get tricked by sensational clickbait, out-of-context video cuts, hidden sponsorships, or photorealistic AI-generated illusions. SpotCheck puts young digital citizens in the role of a Media Detective with a safe, offline, simulated social video feed where every case is a visual mystery waiting to be solved.

HOW TO PLAY & INVESTIGATE
• Scroll the Photorealistic Video Feed: Watch cinema-grade, Ken Burns animated video scenarios modeled after real-world viral trends—from AI-manipulated wildlife and deepfake press briefings to verified NASA Juno space photography—with zero external links, zero tracking, and zero real social media hazards.
• Activate the 2.6x Forensic Optical Lens: Pause any video and engage the Detective Magnifying Glass to scan interactive hotspots and inspect 2.6x optical zoom crops of physical artifacts directly on the frame.
• Read Laterally in the Comments: Open the simulated comments drawer to separate skeptical fact-checkers from automated hype bots.
• Run a Source Check: Inspect creator bios, account history, verification status, and original upload dates using the S.I.F.T. framework.
• Submit Your Verdict: Classify each case as Credible & Verified, Clickbait / Exaggerated, Out-of-Context Clip, AI-Generated / Manipulated, or Sponsored Bias. Receive immediate feedback and earn Detective XP!

KEY LEARNING FEATURES
• The S.I.F.T. Field Guide: Master the four gold-standard pillars of digital verification—Stop, Investigate the Source, Find Better Coverage, and Trace Claims to the Original Context.
• Red Flag Encyclopedia: Explore visual checklists for spotting AI video glitches (fused claws, asymmetric reflections, blurry lip-sync jawlines, garbled background text), emotional rage-bait headlines, and selective jump-cuts.
• Interactive Clickbait Headline Lab: Experiment in the sandbox by swapping sensational alarm words into neutral news stories to see firsthand how clickbait manipulates curiosity and emotion.
• Detective Dossier & Solved Case Archive: Track accuracy rates, review solved photorealistic case evidence, level up from Junior Fact Finder to Chief Truth Officer, and unlock 6 specialized detective badges.
• Google TV Couch Co-Op Support: Full D-pad remote navigation and 10-foot widescreen forensic HUD for living rooms and classrooms.

BUILT FOR SAFETY & PRIVACY
• 100% Offline & Self-Contained: All photorealistic video scenarios, comments, and forensic clues are bundled directly inside the app.
• No Data Collection: SpotCheck does not collect personal information, require an account, access the camera/microphone, or display third-party advertisements.
```

---

## 3. Release Notes (Version 12.0 / Version Code 12)

```text
<en-US>
• Upgraded all 6 media detective cases with photorealistic photography, Ken Burns cinema motion, and real-time forensic HUD telemetry.
• Added 2.6x Forensic Optical Zoom Loupe inside the Clue Inspector to examine physical AI artifacts, shadows, and archival timestamps.
• Updated all Phone, Tablet, and Google TV store visuals, in-app TV banner, and Solved Case Archive thumbnails.
• Updated to Version 12.0 (versionCode 12).
</en-US>
```

---

## 4. Play Console Categorization & Policy Declarations

* **App Category:** Education
* **Tags:** Media Literacy, Critical Thinking, Educational Game, Kids, Fact Checking
* **Target Audience & Content:** Ages 9–12, 13–15 (Pre-teens & Middle Schoolers)
* **Ads Declaration:** No, my app does not contain ads.
* **Data Safety Section:** No data collected or shared. All progress and badges are stored strictly locally on the device via Android Room SQLite database.
