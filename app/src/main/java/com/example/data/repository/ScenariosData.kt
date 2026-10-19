package com.example.data.repository

import com.example.data.model.CaseCategory
import com.example.data.model.CaseVerdict
import com.example.data.model.ClueType
import com.example.data.model.DetectionClue
import com.example.data.model.DetectiveBadge
import com.example.data.model.DifficultyLevel
import com.example.data.model.MediaCase
import com.example.data.model.SiftLesson
import com.example.data.model.SimulatedComment
import com.example.data.model.SourceInvestigation
import com.example.data.model.VideoVisualType

object ScenariosData {

    val defaultBadges: List<com.example.data.model.Badge>
        get() = com.example.domain.MediaProgressTrackerService().getMasterBadges()

    val sampleCases: List<MediaCase> = listOf(
        MediaCase(
            id = "case_ai_cat",
            title = "SHOCKING: Rare 3-Tailed Neon Cat Discovered in Florida?! 🙀",
            creatorHandle = "@DailyViralAnimals",
            creatorDisplayName = "Daily Viral Wildlife",
            isVerifiedCreator = false,
            followerCount = "1.2M",
            videoDurationSec = 14,
            caption = "Scientists CANNOT explain this biological miracle! Drop a comment if you would adopt one! #rare #animals #shocking #mutant",
            tags = listOf("rare", "animals", "viral", "miracle"),
            audioTrackTitle = "Dramatic Suspense Strings - Royalty Free",
            category = CaseCategory.AI_MANIPULATED,
            difficulty = DifficultyLevel.ROOKIE,
            visualType = VideoVisualType.AI_MUTANT_ANIMAL,
            primarySceneColor = 0xFF6B21A8,
            secondarySceneColor = 0xFF06B6D4,
            clues = listOf(
                DetectionClue(
                    id = "clue_cat_paws",
                    title = "Warped Paw & Extra Claws",
                    type = ClueType.VISUAL_ARTIFACT,
                    hint = "Inspect the front paws carefully as the animal steps forward.",
                    explanation = "AI image and video generators frequently struggle with animal paws and human hands. Notice how the paw has 6 claws that blend unnaturally into the rug fibers.",
                    normX = 0.38f,
                    normY = 0.62f
                ),
                DetectionClue(
                    id = "clue_cat_eyes",
                    title = "Unnatural Asynchronous Blink",
                    type = ClueType.VISUAL_ARTIFACT,
                    hint = "Look closely at the cat's eyes when it faces the camera.",
                    explanation = "Real cats blink both eyes symmetrically. Here, the left eye morphs and blurs without an actual eyelid crease—a telltale sign of AI video interpolation.",
                    normX = 0.52f,
                    normY = 0.35f
                ),
                DetectionClue(
                    id = "clue_cat_voice",
                    title = "Robotic Synthetic Voiceover",
                    type = ClueType.AUDIO_SYNTHETIC,
                    hint = "Listen to the pronunciation and robotic rhythm in the narration.",
                    explanation = "The narrator mispronounces 'Florida' with strange cadence and has zero natural breath intake, indicating an automated text-to-speech script.",
                    normX = 0.72f,
                    normY = 0.80f
                )
            ),
            comments = listOf(
                SimulatedComment(
                    id = "c1",
                    author = "Maya_Sleuth",
                    handle = "@mayakids",
                    text = "Wait... pause at 0:03! The tail splits into 3 but the shadow on the floor only shows 1 tail! 🤨",
                    likesCount = "4.2K",
                    isSkeptical = true
                ),
                SimulatedComment(
                    id = "c2",
                    author = "SparkyGamer",
                    handle = "@sparky99",
                    text = "OMG IS THIS REAL?? I WANT ONE FOR CHRISTMAS!!",
                    likesCount = "1.8K",
                    isSkeptical = false
                ),
                SimulatedComment(
                    id = "c3",
                    author = "User_9021029",
                    handle = "@bot_xyz44",
                    text = "Great video! Visit link in my bio to win free robux 🎁",
                    likesCount = "12",
                    isBotLike = true
                )
            ),
            sourceInvestigation = SourceInvestigation(
                originalCreator = "Midjourney Prompt Hobbyist",
                accountType = "Unverified repost bot account",
                creationDate = "3 weeks ago",
                originalUploadDate = "Yesterday",
                verificationNotes = "No accredited biology journal, wildlife sanctuary, or news agency reports a 3-tailed domestic cat.",
                realContext = "This clip was created using an AI video model (Runway Gen-2) with the prompt 'hyperrealistic neon purple cat with three tails sitting on sofa'. It went viral when repost accounts removed the #AI tag.",
                siftLesson = SiftLesson(
                    stop = "Notice feelings of amazement or disbelief—that's when our guard is lowest.",
                    investigate = "Check the creator profile: does this account do actual biology, or just viral engagement bait?",
                    findBetter = "Search reputable science sites (National Geographic Kids, Smithsonian) for 'three tailed cat'. Nothing exists!",
                    traceOriginal = "Check the watermark or reverse-image search: original post had the hashtag #AIGeneratedArt."
                )
            ),
            correctVerdict = CaseVerdict.AI_MANIPULATED,
            takeawayRule = "Always count paws, fingers, and inspect shadows when an animal or human does something biologically impossible!"
        ),
        MediaCase(
            id = "case_disaster_cut",
            title = "URGENT: Terrifying Monster Storm Destroys Coastal City TODAY! ⚠️🌊",
            creatorHandle = "@BreakingDisasterNow",
            creatorDisplayName = "Disaster Alerts 24/7",
            isVerifiedCreator = false,
            followerCount = "890K",
            videoDurationSec = 16,
            caption = "Stay safe everyone! Evacuate immediately! Like and share to warn your family! 🌪️🚨 #breaking #storm #today #emergency",
            tags = listOf("breaking", "storm", "urgent", "weather"),
            audioTrackTitle = "Siren Sound FX & Screaming Crowd",
            category = CaseCategory.OUT_OF_CONTEXT,
            difficulty = DifficultyLevel.DETECTIVE,
            visualType = VideoVisualType.DISASTER_OLD_FOOTAGE,
            primarySceneColor = 0xFF1E293B,
            secondarySceneColor = 0xFFEF4444,
            clues = listOf(
                DetectionClue(
                    id = "clue_storm_billboard",
                    title = "Movie Poster From 2011",
                    type = ClueType.DATE_TIMELINE,
                    hint = "Look at the background street signs and theater marquee.",
                    explanation = "In the background, a cinema marquee advertises 'Harry Potter and the Deathly Hallows Part 2'. That film came out in 2011! This cannot be happening today.",
                    normX = 0.24f,
                    normY = 0.45f
                ),
                DetectionClue(
                    id = "clue_storm_crop",
                    title = "Cropped TV Station Watermark",
                    type = ClueType.MISSING_CONTEXT,
                    hint = "Notice the blurry blurred rectangle in the top right corner.",
                    explanation = "The uploader placed a blur filter over the original news broadcast watermark from 2012 to hide the date stamp.",
                    normX = 0.86f,
                    normY = 0.14f
                ),
                DetectionClue(
                    id = "clue_storm_audio",
                    title = "Recycled Hollywood Disaster Audio",
                    type = ClueType.AUDIO_SYNTHETIC,
                    hint = "Listen to the sirens and tornado alarms. Are they local?",
                    explanation = "The siren track is a famous free stock sound effect used in movies, overlaid to trigger immediate panic in viewers.",
                    normX = 0.65f,
                    normY = 0.75f
                )
            ),
            comments = listOf(
                SimulatedComment(
                    id = "c4",
                    author = "CoastGuardKid",
                    handle = "@coastal_kid",
                    text = "I literally live here right now. The sun is shining and it's 75 degrees! STOP SPREADING FAKE PANIC!",
                    likesCount = "8.9K",
                    isSkeptical = true,
                    badge = "Local Resident"
                ),
                SimulatedComment(
                    id = "c5",
                    author = "PanicShare",
                    handle = "@grandma_dot",
                    text = "Praying for everyone please stay safe sharing to all my group chats!!",
                    likesCount = "3.1K",
                    isSkeptical = false
                )
            ),
            sourceInvestigation = SourceInvestigation(
                originalCreator = "Associated Press Archives",
                accountType = "Clickbait scare account",
                creationDate = "3 months ago",
                originalUploadDate = "4 hours ago",
                verificationNotes = "Official National Weather Service reports no severe storms or coastal flooding in the area today.",
                realContext = "This is authentic news footage, but from Hurricane Sandy in October 2012. It was re-uploaded during normal rainy weather to farm panicked shares and views.",
                siftLesson = SiftLesson(
                    stop = "Strong negative emotions like fear and panic make people share without checking.",
                    investigate = "Check local official weather radars before forwarding warnings.",
                    findBetter = "Look up the National Weather Service (weather.gov) or local emergency services.",
                    traceOriginal = "Clues in the scenery (cars, movies, clothing) often reveal the true decade of the recording."
                )
            ),
            correctVerdict = CaseVerdict.OUT_OF_CONTEXT,
            takeawayRule = "Real footage can still be fake news when someone changes the date or location to scare you!"
        ),
        MediaCase(
            id = "case_miracle_drink",
            title = "DRINK THIS BEFORE SLEEP & Pass Every Math Exam With 100%! 🧠⚡",
            creatorHandle = "@BioHackGenius",
            creatorDisplayName = "Brain Hacks Daily",
            isVerifiedCreator = false,
            followerCount = "450K",
            videoDurationSec = 15,
            caption = "Schools DON'T want you to know about this secret student focus tea! 🤫 (Link in bio for 40% off!) #studytok #examhacks #smart #genius",
            tags = listOf("studytok", "lifehack", "school", "exam"),
            audioTrackTitle = "Upbeat Lo-Fi Study Beats",
            category = CaseCategory.CLICKBAIT,
            difficulty = DifficultyLevel.ROOKIE,
            visualType = VideoVisualType.MIRACLE_CURE_SNACK,
            primarySceneColor = 0xFF0D9488,
            secondarySceneColor = 0xFFF59E0B,
            clues = listOf(
                DetectionClue(
                    id = "clue_cure_headline",
                    title = "Impossible 'Zero Effort' Promise",
                    type = ClueType.EMOTIONAL_HEADLINE,
                    hint = "Examine the main promise made in the title card.",
                    explanation = "Clickbait often promises miraculous results with zero work ('100% on math without studying'). No beverage can teach your brain algebraic formulas overnight!",
                    normX = 0.50f,
                    normY = 0.22f
                ),
                DetectionClue(
                    id = "clue_cure_doctor",
                    title = "Costume Lab Coat with No Credentials",
                    type = ClueType.SOURCE_ANONYMITY,
                    hint = "Check the 'expert' speaker's attire and identity badge.",
                    explanation = "The speaker wears a generic costume doctor's coat, but there is no name tag, medical board license, or affiliated university mentioned.",
                    normX = 0.48f,
                    normY = 0.52f
                ),
                DetectionClue(
                    id = "clue_cure_affiliate",
                    title = "Hidden Sales Pitch & Affiliate Link",
                    type = ClueType.MISSING_CONTEXT,
                    hint = "Look at the bottom caption and call to action.",
                    explanation = "The entire video is an advertisement disguised as study advice. The creator earns money whenever viewers click the 'Link in Bio' to buy overpriced tea.",
                    normX = 0.35f,
                    normY = 0.88f
                )
            ),
            comments = listOf(
                SimulatedComment(
                    id = "c6",
                    author = "BioTeacherSam",
                    handle = "@mrsam_science",
                    text = "Science teacher here: This is literally just chamomile tea and apple juice. Getting 8 hours of sleep helps memory, but tea won't solve quadratic equations!",
                    likesCount = "11.2K",
                    isSkeptical = true,
                    badge = "Educator"
                ),
                SimulatedComment(
                    id = "c7",
                    author = "DesperateStudent",
                    handle = "@test_tomorrow",
                    text = "I ordered 3 bottles, hoping this helps with my finals...",
                    likesCount = "340",
                    isSkeptical = false
                )
            ),
            sourceInvestigation = SourceInvestigation(
                originalCreator = "Influencer Affiliate Marketing Network",
                accountType = "Commercial sales funnel",
                creationDate = "6 months ago",
                originalUploadDate = "2 days ago",
                verificationNotes = "Not approved by medical or pediatric organizations. Disclaimers in tiny gray font say 'results not guaranteed'.",
                realContext = "Commercial promotional video made to sell ordinary flavored herbal tea bags for $39.99 to stressed students.",
                siftLesson = SiftLesson(
                    stop = "Ask: Is someone trying to sell me something while scaring or promising quick fixes?",
                    investigate = "Check if the 'expert' has real credentials or is just an actor in a costume.",
                    findBetter = "Consult reputable learning tips from universities or school counselors.",
                    traceOriginal = "Notice affiliate codes or 'link in bio' calls to action."
                )
            ),
            correctVerdict = CaseVerdict.CLICKBAIT_EXAGGERATED,
            takeawayRule = "If a product claims to give you superpowers with zero effort, it's selling you something—not educating you!"
        ),
        MediaCase(
            id = "case_authentic_nasa",
            title = "NASA Webb Telescope Reveals Stunning Auroral Storm on Jupiter 🔭🪐",
            creatorHandle = "@NASAWebbOfficial",
            creatorDisplayName = "NASA James Webb Space Telescope",
            isVerifiedCreator = true,
            followerCount = "9.4M",
            videoDurationSec = 18,
            caption = "New composite infrared observations processed by Goddard Space Flight Center researchers show high-altitude polar auroras on Jupiter. Peer-reviewed research available in Nature Astronomy.",
            tags = listOf("space", "astronomy", "NASA", "JWST", "Jupiter"),
            audioTrackTitle = "Ambient Cosmos Synth - Official NASA Media Lab",
            category = CaseCategory.CREDIBLE_FACT,
            difficulty = DifficultyLevel.ROOKIE,
            visualType = VideoVisualType.AUTHENTIC_SCIENCE_SOLAR,
            primarySceneColor = 0xFF1E1B4B,
            secondarySceneColor = 0xFF10B981,
            clues = listOf(
                DetectionClue(
                    id = "clue_nasa_verified",
                    title = "Official Verified Agency Badge",
                    type = ClueType.SOURCE_ANONYMITY,
                    hint = "Check the account handle and verification shield.",
                    explanation = "The account possesses the official blue verification badge and is linked directly to nasa.gov with public government accountability.",
                    normX = 0.22f,
                    normY = 0.16f
                ),
                DetectionClue(
                    id = "clue_nasa_citation",
                    title = "Peer-Reviewed Science Citation",
                    type = ClueType.MISSING_CONTEXT,
                    hint = "Inspect how the claims are supported in the description.",
                    explanation = "Instead of sensational claims, the post cites an actual scientific paper in 'Nature Astronomy' and names the university research team.",
                    normX = 0.50f,
                    normY = 0.85f
                ),
                DetectionClue(
                    id = "clue_nasa_nuance",
                    title = "Honest Explanation of Infrared Color",
                    type = ClueType.VISUAL_ARTIFACT,
                    hint = "Look at the explanation of the glowing colors.",
                    explanation = "The video openly explains that the image is a 'false-color composite' of infrared light mapped to visible hues so human eyes can see heat radiation.",
                    normX = 0.55f,
                    normY = 0.48f
                )
            ),
            comments = listOf(
                SimulatedComment(
                    id = "c8",
                    author = "AstroClubYouth",
                    handle = "@space_kids",
                    text = "Our school science club read the Nature paper today! The magnetosphere physics are mind-blowing!",
                    likesCount = "6.4K",
                    isSkeptical = false
                ),
                SimulatedComment(
                    id = "c9",
                    author = "SkyWatcher_99",
                    handle = "@sky_99",
                    text = "Love that NASA explains how the infrared filters work instead of just hyping it up.",
                    likesCount = "3.2K",
                    isSkeptical = false
                )
            ),
            sourceInvestigation = SourceInvestigation(
                originalCreator = "NASA / ESA / CSA Research Team",
                accountType = "Verified International Space Agency",
                creationDate = "8 years ago",
                originalUploadDate = "Yesterday",
                verificationNotes = "Confirmed on official government portals (nasa.gov/webb) and verified by independent space observatories worldwide.",
                realContext = "Authentic scientific discovery made by the James Webb Space Telescope NIRCam instrument.",
                siftLesson = SiftLesson(
                    stop = "Not everything online is fake! Learn to recognize authentic, rigorous journalism.",
                    investigate = "Check the author's history: do they have actual expertise in this field?",
                    findBetter = "Look for cross-confirmation from other established scientific organizations.",
                    traceOriginal = "Authentic sources provide links directly to the original raw data and research papers."
                )
            ),
            correctVerdict = CaseVerdict.ACCURATE_CREDIBLE,
            takeawayRule = "Credible sources cite their evidence, admit limitations, and don't need all-caps shock headlines to get attention!"
        ),
        MediaCase(
            id = "case_hero_rescue",
            title = "10-YEAR-OLD Hero Stops Runaway Train With His Bare Hands?! 🚴😱",
            creatorHandle = "@ViralHeroMoments",
            creatorDisplayName = "Heartwarming Heroes",
            isVerifiedCreator = false,
            followerCount = "2.1M",
            videoDurationSec = 17,
            caption = "Watch until the end! This brave kid stood on the tracks to stop a disaster! Faith in humanity restored! ❤️🙏 #hero #brave #viral #faith",
            tags = listOf("hero", "rescue", "brave", "faithinhumanity"),
            audioTrackTitle = "Emotional Piano & Hans Zimmer Style Brass",
            category = CaseCategory.OUT_OF_CONTEXT,
            difficulty = DifficultyLevel.MASTER_FORENSIC,
            visualType = VideoVisualType.STAGED_HERO_RESCUE,
            primarySceneColor = 0xFF7C2D12,
            secondarySceneColor = 0xFFF97316,
            clues = listOf(
                DetectionClue(
                    id = "clue_train_speed",
                    title = "Train Already Decelerating for Station",
                    type = ClueType.MISSING_CONTEXT,
                    hint = "Observe the yellow platform lines and train wheels.",
                    explanation = "Notice the train wheels already had sparks applied by normal station air brakes 200 feet earlier. The train was stopping anyway at a scheduled red signal.",
                    normX = 0.62f,
                    normY = 0.58f
                ),
                DetectionClue(
                    id = "clue_train_driver",
                    title = "Conductor Waving Casually",
                    type = ClueType.VISUAL_ARTIFACT,
                    hint = "Look inside the front cab window at the conductor's face.",
                    explanation = "The train conductor is smiling and waving at station tourists, completely unalarmed. This is an open-air scenic heritage railroad running at 5 mph!",
                    normX = 0.44f,
                    normY = 0.38f
                ),
                DetectionClue(
                    id = "clue_staged_audio",
                    title = "Artificial Screams Added in Post-Production",
                    type = ClueType.AUDIO_SYNTHETIC,
                    hint = "Listen to the crowd audio versus people's relaxed body language.",
                    explanation = "The audio track includes frantic screaming sound effects, but people on the platform in the background are calmly eating ice cream.",
                    normX = 0.78f,
                    normY = 0.82f
                )
            ),
            comments = listOf(
                SimulatedComment(
                    id = "c10",
                    author = "TrainBuff_Alex",
                    handle = "@alextrains",
                    text = "Guys, this is the Strasburg Railroad tourist line in Pennsylvania. The train maxes out at 8 miles per hour and stops at that exact crossing every 20 minutes!",
                    likesCount = "15.4K",
                    isSkeptical = true,
                    badge = "Railroad Enthusiast"
                ),
                SimulatedComment(
                    id = "c11",
                    author = "WholesomeLover",
                    handle = "@wholesome_vibes",
                    text = "Give this boy a medal immediately!! Crying real tears 😭😭",
                    likesCount = "5.1K",
                    isSkeptical = false
                )
            ),
            sourceInvestigation = SourceInvestigation(
                originalCreator = "Tourist Family Home Video",
                accountType = "Emotional engagement farm",
                creationDate = "1 year ago",
                originalUploadDate = "3 days ago",
                verificationNotes = "Local railway company confirmed this was a harmless photo-op at a heritage tourist museum, not a runaway emergency.",
                realContext = "A wholesome vacation video of a boy waving a toy flag at a slow scenic steam train was re-edited with disaster music to create fake 'hero' content.",
                siftLesson = SiftLesson(
                    stop = "Emotional manipulation doesn't just use anger or fear—it also uses heartwarming stories to lower our skepticism.",
                    investigate = "Look at the background actors: do their reactions match the supposed danger?",
                    findBetter = "Search for local news reports: a child stopping a real runaway train would be front page news across the globe.",
                    traceOriginal = "Check the location: heritage railways and tourist spots frequently have vintage trains operating at walking speed."
                )
            ),
            correctVerdict = CaseVerdict.OUT_OF_CONTEXT,
            takeawayRule = "Even heartwarming 'feel-good' videos are often staged or taken out of context to harvest likes and shares!"
        ),
        MediaCase(
            id = "case_deepfake_speech",
            title = "PRESIDENT ANNOUNCES: All Homework Is Officially BANNED! 🎉📚",
            creatorHandle = "@SchoolMemesHQ",
            creatorDisplayName = "High School & Middle School Memes",
            isVerifiedCreator = false,
            followerCount = "3.4M",
            videoDurationSec = 14,
            caption = "PACK YOUR BAGS TEACHERS! IT IS LAW AS OF TOMORROW MORNING! 😱🔥 Tag your bestie! #school #nohomework #president #news",
            tags = listOf("school", "homework", "president", "memes"),
            audioTrackTitle = "Official Presidential Podium Audio (AI Cloned)",
            category = CaseCategory.AI_MANIPULATED,
            difficulty = DifficultyLevel.DETECTIVE,
            visualType = VideoVisualType.SPACE_ALIEN_HOAX, // Uses talking head forensic layout
            primarySceneColor = 0xFF1E3A8A,
            secondarySceneColor = 0xFFDC2626,
            clues = listOf(
                DetectionClue(
                    id = "clue_deepfake_lips",
                    title = "Lip-Sync Blur & Audio Lag",
                    type = ClueType.VISUAL_ARTIFACT,
                    hint = "Focus your eyes on the mouth movements when speaking consonants like 'P' and 'B'.",
                    explanation = "Notice the slight digital distortion around the lips and chin. The mouth movements lag by approximately 200 milliseconds behind the speech.",
                    normX = 0.50f,
                    normY = 0.44f
                ),
                DetectionClue(
                    id = "clue_deepfake_tone",
                    title = "Monotone AI Voice Synthesis",
                    type = ClueType.AUDIO_SYNTHETIC,
                    hint = "Listen to the pitch inflection and breathing pauses.",
                    explanation = "The voice sounds convincing at first, but has a flat, metallic cadence without natural human pauses, throat clearing, or room echo.",
                    normX = 0.70f,
                    normY = 0.70f
                ),
                DetectionClue(
                    id = "clue_deepfake_civics",
                    title = "Impossible Legal Authority",
                    type = ClueType.MISSING_CONTEXT,
                    hint = "Think about civics: who decides school homework policies?",
                    explanation = "Under the constitution, the President cannot dictate local school homework—school boards and states do. A major policy change like this would be on every news channel worldwide.",
                    normX = 0.32f,
                    normY = 0.88f
                )
            ),
            comments = listOf(
                SimulatedComment(
                    id = "c12",
                    author = "CivicsKid_9",
                    handle = "@civics_smart",
                    text = "Fake! This is a deepfake made with ElevenLabs voice cloning. The original press conference was about infrastructure funding 6 months ago.",
                    likesCount = "18.1K",
                    isSkeptical = true,
                    badge = "Fact Checker"
                ),
                SimulatedComment(
                    id = "c13",
                    author = "MiddleSchooler101",
                    handle = "@no_homework_pls",
                    text = "I showed this to my math teacher and she made me do 20 extra problems 💀",
                    likesCount = "9.4K",
                    isSkeptical = false
                )
            ),
            sourceInvestigation = SourceInvestigation(
                originalCreator = "AI Meme Generator on Reddit",
                accountType = "Viral comedy & satire meme page",
                creationDate = "2 years ago",
                originalUploadDate = "Yesterday",
                verificationNotes = "Zero national or international news organizations reported this announcement. Official WhiteHouse.gov transcript shows no mention of homework bans.",
                realContext = "Created as a humorous audio clone deepfake for April Fools / student entertainment. Became misinformation when reshared by accounts without satire disclaimers.",
                siftLesson = SiftLesson(
                    stop = "When news seems 'too good to be true' (like no more homework), pause and verify immediately.",
                    investigate = "Check the source: is a meme account the first to report major international legislation?",
                    findBetter = "Look up reputable journalism (PBS NewsHour, Associated Press, BBC Kids).",
                    traceOriginal = "Examine the mouth, neck, and collar lines for deepfake seam artifacts."
                )
            ),
            correctVerdict = CaseVerdict.AI_MANIPULATED,
            takeawayRule = "If a world leader appears in a video saying something unbelievable, check reputable news outlets before believing it!"
        )
    )
}
