package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.SolvedCaseEntity
import com.example.data.local.UserStatsEntity
import com.example.data.model.Badge
import com.example.data.model.CaseVerdict
import com.example.data.model.DetectionClue
import com.example.data.model.DetectiveBadge
import com.example.data.model.MediaCase
import com.example.data.model.FactCheckResult
import com.example.data.repository.MediaDetectiveRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class AppScreen {
    FEED,
    FIELD_GUIDE,
    PROFILE,
    SANDBOX
}

data class SpotCheckUiState(
    val currentScreen: AppScreen = AppScreen.FEED,
    val currentCaseIndex: Int = 0,
    val cases: List<MediaCase> = emptyList(),
    val isPlaying: Boolean = true,
    val videoProgressSec: Float = 0f,
    val isInspectionMode: Boolean = false,
    val discoveredClueIds: Set<String> = emptySet(),
    val inspectedClue: DetectionClue? = null,
    val showCommentsSheet: Boolean = false,
    val showSourceCheckSheet: Boolean = false,
    val showVerdictDialog: Boolean = false,
    val latestSubmissionResult: FactCheckResult? = null,
    val newlyUnlockedBadges: List<Badge> = emptyList(),
    val solvedCases: Map<String, SolvedCaseEntity> = emptyMap(),
    val badges: List<DetectiveBadge> = emptyList(),
    val userStats: UserStatsEntity = UserStatsEntity(),
    // Sandbox Lab State
    val sandboxHeadline: String = "SHOCKING: Aliens Caught on Camera in School Cafeteria!",
    val sandboxIsClickbait: Boolean = true,
    val sandboxHasSource: Boolean = false,
    val sandboxHasAiGlitch: Boolean = true
) {
    val currentCase: MediaCase?
        get() = cases.getOrNull(currentCaseIndex)

    val currentCaseSolved: SolvedCaseEntity?
        get() = currentCase?.let { solvedCases[it.id] }

    val totalXp: Int
        get() = solvedCases.values.sumOf { it.earnedXp }

    val accuracyPercent: Int
        get() {
            if (solvedCases.isEmpty()) return 100
            val correctCount = solvedCases.values.count { it.isCorrect }
            return (correctCount * 100) / solvedCases.size
        }

    val rankTitle: String
        get() = when {
            totalXp >= 800 -> "Chief Forensic Fact-Checker"
            totalXp >= 500 -> "Master Media Sleuth"
            totalXp >= 250 -> "Senior Fact Detective"
            totalXp >= 100 -> "Junior Investigator"
            else -> "Rookie Sleuth"
        }
}

class SpotCheckViewModel(
    private val repository: MediaDetectiveRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SpotCheckUiState(
            cases = repository.getAllCases()
        )
    )
    val uiState: StateFlow<SpotCheckUiState> = _uiState.asStateFlow()

    private var playbackJob: Job? = null

    init {
        startPlaybackSimulation()
        observePersistence()
    }

    private fun observePersistence() {
        viewModelScope.launch {
            repository.getSolvedCases().collect { solvedList ->
                _uiState.update { current ->
                    current.copy(solvedCases = solvedList.associateBy { it.caseId })
                }
            }
        }

        viewModelScope.launch {
            repository.getBadges().collect { badgeList ->
                _uiState.update { current ->
                    current.copy(badges = badgeList)
                }
            }
        }

        viewModelScope.launch {
            repository.getUserStats().collect { stats ->
                _uiState.update { current ->
                    current.copy(userStats = stats)
                }
            }
        }
    }

    private fun startPlaybackSimulation() {
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            while (isActive) {
                delay(100)
                if (_uiState.value.isPlaying) {
                    _uiState.update { current ->
                        val duration = current.currentCase?.videoDurationSec?.toFloat() ?: 15f
                        val next = (current.videoProgressSec + 0.1f)
                        val looped = if (next > duration) 0f else next
                        current.copy(videoProgressSec = looped)
                    }
                }
            }
        }
    }

    fun togglePlayback() {
        _uiState.update { it.copy(isPlaying = !it.isPlaying) }
    }

    fun setPlayback(playing: Boolean) {
        _uiState.update { it.copy(isPlaying = playing) }
    }

    fun seekTo(progress: Float) {
        _uiState.update { it.copy(videoProgressSec = progress) }
    }

    fun toggleInspectionMode() {
        _uiState.update {
            val nextState = !it.isInspectionMode
            it.copy(
                isInspectionMode = nextState,
                // Pause playback automatically when inspecting clues
                isPlaying = if (nextState) false else it.isPlaying
            )
        }
    }

    fun selectCaseIndex(index: Int) {
        if (index in 0 until _uiState.value.cases.size) {
            _uiState.update {
                it.copy(
                    currentCaseIndex = index,
                    videoProgressSec = 0f,
                    isInspectionMode = false,
                    inspectedClue = null,
                    showCommentsSheet = false,
                    showSourceCheckSheet = false,
                    showVerdictDialog = false,
                    latestSubmissionResult = null
                )
            }
        }
    }

    fun nextCase() {
        val nextIdx = (_uiState.value.currentCaseIndex + 1) % _uiState.value.cases.size
        selectCaseIndex(nextIdx)
    }

    fun previousCase() {
        val prevIdx = if (_uiState.value.currentCaseIndex - 1 < 0) {
            _uiState.value.cases.size - 1
        } else {
            _uiState.value.currentCaseIndex - 1
        }
        selectCaseIndex(prevIdx)
    }

    fun onClueClicked(clue: DetectionClue) {
        _uiState.update { current ->
            val updatedDiscovered = current.discoveredClueIds + clue.id
            current.copy(
                discoveredClueIds = updatedDiscovered,
                inspectedClue = clue,
                isPlaying = false
            )
        }
    }

    fun dismissClueDetail() {
        _uiState.update { it.copy(inspectedClue = null) }
    }

    fun openCommentsSheet(open: Boolean) {
        _uiState.update { it.copy(showCommentsSheet = open) }
    }

    fun openSourceCheckSheet(open: Boolean) {
        _uiState.update { it.copy(showSourceCheckSheet = open) }
    }

    fun openVerdictDialog(open: Boolean) {
        _uiState.update {
            it.copy(
                showVerdictDialog = open,
                isPlaying = if (open) false else it.isPlaying
            )
        }
    }

    fun submitVerdict(selectedVerdict: CaseVerdict, userNotes: String = "") {
        val currentCase = _uiState.value.currentCase ?: return
        val cluesFound = _uiState.value.discoveredClueIds

        viewModelScope.launch {
            val payload = repository.submitVerdict(
                mediaCase = currentCase,
                selectedVerdict = selectedVerdict,
                discoveredClues = cluesFound,
                userNotes = userNotes
            )
            _uiState.update {
                it.copy(
                    latestSubmissionResult = payload.factCheckResult,
                    newlyUnlockedBadges = payload.newlyUnlockedBadges
                )
            }
        }
    }

    fun dismissResultModal() {
        _uiState.update {
            it.copy(
                latestSubmissionResult = null,
                newlyUnlockedBadges = emptyList(),
                showVerdictDialog = false
            )
        }
    }

    fun navigateTo(screen: AppScreen) {
        _uiState.update {
            it.copy(
                currentScreen = screen,
                // pause video if leaving feed
                isPlaying = if (screen != AppScreen.FEED) false else it.isPlaying
            )
        }
    }

    // Sandbox lab modifiers
    fun updateSandboxHeadline(headline: String) {
        _uiState.update { it.copy(sandboxHeadline = headline) }
    }

    fun toggleSandboxClickbait(value: Boolean) {
        _uiState.update { it.copy(sandboxIsClickbait = value) }
    }

    fun toggleSandboxSource(value: Boolean) {
        _uiState.update { it.copy(sandboxHasSource = value) }
    }

    fun toggleSandboxAiGlitch(value: Boolean) {
        _uiState.update { it.copy(sandboxHasAiGlitch = value) }
    }

    class Factory(private val repository: MediaDetectiveRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SpotCheckViewModel(repository) as T
        }
    }
}
