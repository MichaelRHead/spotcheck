package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SpotCheckDao {
    @Query("SELECT * FROM solved_cases")
    fun getAllSolvedCases(): Flow<List<SolvedCaseEntity>>

    @Query("SELECT * FROM solved_cases")
    suspend fun getSolvedCasesSnapshot(): List<SolvedCaseEntity>

    @Query("SELECT * FROM solved_cases WHERE caseId = :caseId LIMIT 1")
    suspend fun getSolvedCase(caseId: String): SolvedCaseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSolvedCase(solvedCase: SolvedCaseEntity)

    @Query("SELECT * FROM user_stats WHERE id = 1")
    fun getUserStats(): Flow<UserStatsEntity?>

    @Query("SELECT * FROM user_stats WHERE id = 1")
    suspend fun getUserStatsSnapshot(): UserStatsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserStats(stats: UserStatsEntity)

    @Query("SELECT * FROM unlocked_badges")
    fun getUnlockedBadges(): Flow<List<BadgeEntity>>

    @Query("SELECT * FROM unlocked_badges")
    suspend fun getUnlockedBadgesSnapshot(): List<BadgeEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun unlockBadge(badge: BadgeEntity)

    @Query("DELETE FROM solved_cases")
    suspend fun clearHistory()
}
