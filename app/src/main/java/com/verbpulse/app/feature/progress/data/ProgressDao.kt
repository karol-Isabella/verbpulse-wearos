package com.verbpulse.app.feature.progress.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) para interactuar con la tabla de progreso diario (RF-007, RF-008).
 */
@Dao
interface ProgressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(progress: DailyProgressEntity)

    @Query("SELECT * FROM daily_progress WHERE date = :date LIMIT 1")
    suspend fun getProgressByDate(date: String): DailyProgressEntity?

    @Query("SELECT * FROM daily_progress ORDER BY date DESC LIMIT 1")
    fun getLatestProgress(): Flow<DailyProgressEntity?>

    @Query("SELECT * FROM daily_progress ORDER BY date DESC LIMIT 1")
    suspend fun getLatestProgressSync(): DailyProgressEntity?

    @Query("SELECT * FROM daily_progress ORDER BY date DESC")
    fun getAllProgress(): Flow<List<DailyProgressEntity>>

    @Query("SELECT COALESCE(SUM(wordsLearned), 0) FROM daily_progress")
    fun getTotalWordsLearned(): Flow<Int>

    @Query("SELECT COALESCE(AVG(scorePercentage), 0.0) FROM daily_progress")
    fun getAverageScorePercentage(): Flow<Float>
}
