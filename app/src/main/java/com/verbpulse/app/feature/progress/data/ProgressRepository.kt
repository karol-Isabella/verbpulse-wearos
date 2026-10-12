package com.verbpulse.app.feature.progress.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositorio para gestionar el rendimiento, aciertos y racha acumulada en VerbPulse (RF-006, RF-007, RF-008).
 */
@Singleton
class ProgressRepository @Inject constructor(
    private val progressDao: ProgressDao
) {

    val latestProgress: Flow<DailyProgressEntity?> = progressDao.getLatestProgress()

    val totalWordsLearned: Flow<Int> = progressDao.getTotalWordsLearned()

    val averageScorePercentage: Flow<Float> = progressDao.getAverageScorePercentage()

    val currentStreak: Flow<Int> = latestProgress.map { latest ->
        if (latest == null) return@map 0
        val today = LocalDate.now()
        val latestDate = try {
            LocalDate.parse(latest.date)
        } catch (e: Exception) {
            null
        }

        if (latestDate != null) {
            val daysBetween = ChronoUnit.DAYS.between(latestDate, today)
            // Si el último registro fue hoy o ayer, la racha se mantiene vigente
            if (daysBetween <= 1) latest.streakDays else 0
        } else {
            0
        }
    }

    /**
     * Registra el resultado del mini quiz nocturno/diario y actualiza la racha consecutiva (RF-006, RF-007).
     */
    suspend fun recordQuizResult(
        category: String,
        totalQuestions: Int,
        correctAnswers: Int,
        wordsLearned: Int
    ): DailyProgressEntity {
        val today = LocalDate.now()
        val todayStr = today.toString()
        val yesterdayStr = today.minusDays(1).toString()

        val latest = progressDao.getLatestProgressSync()

        val calculatedStreak = when {
            latest == null -> 1
            latest.date == todayStr -> latest.streakDays // Mismo día: mantiene racha actual
            latest.date == yesterdayStr -> latest.streakDays + 1 // Día consecutivo: suma 1
            else -> 1 // Racha rota (> 1 día): reinicia en 1
        }

        val scorePercentage = if (totalQuestions > 0) {
            (correctAnswers.toFloat() / totalQuestions) * 100f
        } else {
            0f
        }

        val entry = DailyProgressEntity(
            date = todayStr,
            category = category,
            wordsLearned = wordsLearned,
            totalQuestions = totalQuestions,
            correctAnswers = correctAnswers,
            scorePercentage = scorePercentage,
            streakDays = calculatedStreak,
            timestamp = System.currentTimeMillis()
        )

        progressDao.insertOrUpdate(entry)
        Timber.i("Progreso diario registrado: %s, racha: %d días, acierto: %.1f%%", todayStr, calculatedStreak, scorePercentage)
        return entry
    }

    suspend fun getTodayProgress(): DailyProgressEntity? {
        val todayStr = LocalDate.now().toString()
        return progressDao.getProgressByDate(todayStr)
    }
}
