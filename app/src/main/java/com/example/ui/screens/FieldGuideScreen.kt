package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FindInPage
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FieldGuideScreen(
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("S.I.F.T. Method", "AI & Deepfakes", "Clickbait Traps")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(top = 40.dp)
            .testTag("field_guide_screen")
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFF0284C7),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "MEDIA LITERACY ACADEMY",
                    color = Color(0xFF38BDF8),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Detective Field Guide",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color(0xFF1E293B),
            contentColor = Color(0xFF38BDF8),
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = Color(0xFF38BDF8)
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier.testTag("guide_tab_$index")
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    item {
                        SiftGuideSection()
                    }
                }
                1 -> {
                    item {
                        AiDeepfakeSection()
                    }
                }
                2 -> {
                    item {
                        ClickbaitTrapsSection()
                    }
                }
            }
        }
    }
}

@Composable
private fun SiftGuideSection() {
    Column {
        Text(
            text = "The S.I.F.T. Fact-Checking Technique",
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Invented by media literacy researcher Mike Caulfield, S.I.F.T. is the gold standard used by university researchers and journalists.",
            color = Color(0xFF94A3B8),
            fontSize = 12.sp,
            lineHeight = 17.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        SiftDetailCard(
            letter = "S",
            title = "STOP",
            rule = "When you feel sudden outrage, excitement, or shock, PAUSE.",
            body = "Misinformation is engineered to trigger emotions so you share before thinking. If a post makes you want to immediately scream or share with everyone, that is your signal to stop.",
            color = Color(0xFFEF4444),
            icon = Icons.Default.PanTool
        )

        Spacer(modifier = Modifier.height(12.dp))

        SiftDetailCard(
            letter = "I",
            title = "INVESTIGATE THE SOURCE",
            rule = "Who posted this? What are their credentials?",
            body = "Check the creator profile. Is this a verified scientific institution, or an anonymous meme page created last Tuesday? Look outside their page to see what others say about them.",
            color = Color(0xFFF59E0B),
            icon = Icons.Default.Person
        )

        Spacer(modifier = Modifier.height(12.dp))

        SiftDetailCard(
            letter = "F",
            title = "FIND BETTER COVERAGE",
            rule = "Practice Lateral Reading across multiple tabs.",
            body = "Don't just stay inside one app. Open your browser and search the claim on reputable news sites like Associated Press, BBC Kids, or Snopes to see if multiple independent sources confirm it.",
            color = Color(0xFF3B82F6),
            icon = Icons.Default.FindInPage
        )

        Spacer(modifier = Modifier.height(12.dp))

        SiftDetailCard(
            letter = "T",
            title = "TRACE CLAIMS TO THE ORIGINAL CONTEXT",
            rule = "Find where the clip or quote came from originally.",
            body = "Often a real video from 10 years ago or from a movie set is re-uploaded with a false headline. Look for background clues (car models, billboards, logos) to trace the true event.",
            color = Color(0xFF10B981),
            icon = Icons.Default.History
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun SiftDetailCard(
    letter: String,
    title: String,
    rule: String,
    body: String,
    color: Color,
    icon: ImageVector
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = color.copy(alpha = 0.2f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = letter,
                            color = color,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        color = color,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = rule,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = body,
                color = Color(0xFFCBD5E1),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun AiDeepfakeSection() {
    Column {
        Text(
            text = "AI & Deepfake Spotter's Checklist",
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Generative AI is improving fast, but it leaves behind distinct forensic glitches if you know where to look:",
            color = Color(0xFF94A3B8),
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        ChecklistItem(
            title = "1. Paws, Fingers & Limbs",
            description = "AI video models have difficulty maintaining anatomical consistency. Look for 6 fingers, fused claws, or hands that melt into objects when moving.",
            icon = Icons.Default.SmartToy
        )

        ChecklistItem(
            title = "2. Unnatural Blinking & Eyes",
            description = "Early AI blinks either too rarely, or blinks one eye at a time without realistic eyelid creases. Also check if pupils are circular or misshapen.",
            icon = Icons.Default.Lightbulb
        )

        ChecklistItem(
            title = "3. Lighting & Shadow Physics",
            description = "Does the shadow move when the subject moves? Are reflections in mirrors or puddles pointing in the wrong direction? AI often hallucinates lighting.",
            icon = Icons.Default.AutoAwesome
        )

        ChecklistItem(
            title = "4. Robotic Voice & Breathless Speech",
            description = "Voice clones often lack normal human breath pauses, pitch micro-fluctuations, or room reverberation. Consonant sounds (P, B, M) frequently lag behind lips.",
            icon = Icons.Default.Hearing
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun ChecklistItem(
    title: String,
    description: String,
    icon: ImageVector
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFF7C3AED).copy(alpha = 0.2f),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color(0xFFA78BFA),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    color = Color(0xFFCBD5E1),
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

@Composable
private fun ClickbaitTrapsSection() {
    Column {
        Text(
            text = "Sensational Clickbait Red Flags",
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Clickbait creators care about ad revenue and viral reach, not the truth. Watch for these emotional alarm triggers:",
            color = Color(0xFF94A3B8),
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        ClickbaitRedFlagWord(
            word = "\"DOCTORS DON'T WANT YOU TO KNOW\"",
            category = "Conspiracy Appeal",
            lesson = "Real medical discoveries are published in peer-reviewed journals, not whispered as secrets in TikTok descriptions."
        )

        ClickbaitRedFlagWord(
            word = "\"SHARE BEFORE THEY DELETE THIS!\"",
            category = "False Urgency Trap",
            lesson = "Creates synthetic panic so you quickly forward it without taking 10 seconds to fact-check."
        )

        ClickbaitRedFlagWord(
            word = "\"YOU WON'T BELIEVE WHAT HAPPENED NEXT\"",
            category = "Curiosity Gap Manipulation",
            lesson = "Withholds key context deliberately so you are forced to watch 30 seconds of fluff to find an anti-climactic result."
        )

        ClickbaitRedFlagWord(
            word = "\"100% GUARANTEED WITH ZERO EFFORT\"",
            category = "Miracle Cure Scam",
            lesson = "Whether it is exam scores, fitness, or wealth: legitimate achievements require work. Miracles in bottles are sales pitches."
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun ClickbaitRedFlagWord(
    word: String,
    category: String,
    lesson: String
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFEF4444),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = word,
                    color = Color(0xFFFCA5A5),
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0x33EF4444)
                ) {
                    Text(
                        text = category,
                        color = Color(0xFFF87171),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = lesson,
                color = Color(0xFFE2E8F0),
                fontSize = 12.sp,
                lineHeight = 17.sp
            )
        }
    }
}
