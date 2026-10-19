package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.SpotCheckDatabase
import com.example.data.repository.MediaDetectiveRepository
import com.example.ui.screens.FeedScreen
import com.example.ui.screens.FieldGuideScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SandboxScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.SpotCheckViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val context = LocalContext.current
                val database = SpotCheckDatabase.getDatabase(context)
                val repository = MediaDetectiveRepository(database.spotCheckDao())
                val viewModel: SpotCheckViewModel = viewModel(
                    factory = SpotCheckViewModel.Factory(repository)
                )

                SpotCheckApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun SpotCheckApp(viewModel: SpotCheckViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle back button when away from Feed
    BackHandler(enabled = uiState.currentScreen != AppScreen.FEED) {
        viewModel.navigateTo(AppScreen.FEED)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF0F172A),
                contentColor = Color(0xFF38BDF8),
                tonalElevation = 8.dp,
                windowInsets = WindowInsets.navigationBars,
                modifier = Modifier
                    .height(68.dp)
                    .testTag("main_bottom_nav")
            ) {
                NavigationBarItem(
                    selected = uiState.currentScreen == AppScreen.FEED,
                    onClick = { viewModel.navigateTo(AppScreen.FEED) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.currentScreen == AppScreen.FEED) Icons.Filled.PlayCircle else Icons.Outlined.PlayCircle,
                            contentDescription = "Simulated Feed",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Feed",
                            fontSize = 11.sp,
                            fontWeight = if (uiState.currentScreen == AppScreen.FEED) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color(0xFF38BDF8),
                        indicatorColor = Color(0xFF7C3AED),
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8)
                    ),
                    modifier = Modifier.testTag("nav_item_feed")
                )

                NavigationBarItem(
                    selected = uiState.currentScreen == AppScreen.FIELD_GUIDE,
                    onClick = { viewModel.navigateTo(AppScreen.FIELD_GUIDE) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.currentScreen == AppScreen.FIELD_GUIDE) Icons.Filled.MenuBook else Icons.Outlined.MenuBook,
                            contentDescription = "Field Guide",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Field Guide",
                            fontSize = 11.sp,
                            fontWeight = if (uiState.currentScreen == AppScreen.FIELD_GUIDE) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color(0xFF38BDF8),
                        indicatorColor = Color(0xFF0284C7),
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8)
                    ),
                    modifier = Modifier.testTag("nav_item_guide")
                )

                NavigationBarItem(
                    selected = uiState.currentScreen == AppScreen.SANDBOX,
                    onClick = { viewModel.navigateTo(AppScreen.SANDBOX) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.currentScreen == AppScreen.SANDBOX) Icons.Filled.Science else Icons.Outlined.Science,
                            contentDescription = "Misinfo Lab",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Lab",
                            fontSize = 11.sp,
                            fontWeight = if (uiState.currentScreen == AppScreen.SANDBOX) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color(0xFFF0ABFC),
                        indicatorColor = Color(0xFFC026D3),
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8)
                    ),
                    modifier = Modifier.testTag("nav_item_sandbox")
                )

                NavigationBarItem(
                    selected = uiState.currentScreen == AppScreen.PROFILE,
                    onClick = { viewModel.navigateTo(AppScreen.PROFILE) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.currentScreen == AppScreen.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
                            contentDescription = "Detective Dossier",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Dossier",
                            fontSize = 11.sp,
                            fontWeight = if (uiState.currentScreen == AppScreen.PROFILE) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color(0xFFFBBF24),
                        indicatorColor = Color(0xFFB45309),
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8)
                    ),
                    modifier = Modifier.testTag("nav_item_profile")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF0B0F19))
        ) {
            AnimatedContent(
                targetState = uiState.currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    AppScreen.FEED -> FeedScreen(
                        viewModel = viewModel,
                        uiState = uiState
                    )
                    AppScreen.FIELD_GUIDE -> FieldGuideScreen()
                    AppScreen.SANDBOX -> SandboxScreen(
                        viewModel = viewModel,
                        uiState = uiState
                    )
                    AppScreen.PROFILE -> ProfileScreen(
                        viewModel = viewModel,
                        uiState = uiState
                    )
                }
            }
        }
    }
}
