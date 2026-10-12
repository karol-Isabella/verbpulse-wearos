package com.verbpulse.app.feature.progress.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de Room para registrar el rendimiento diario del usuario en VerbPulse (RF-006, RF-007, RF-008).
 * Permite almacenar histórico de aciertos, palabras aprendidas y racha acumulada.
 */
@Entity(tableName = "daily_progress")
data class DailyProgressEntity(
    @PrimaryKey
    val date: String, // Fecha única del día en formato "yyyy-MM-dd"
    val category: String, // Categoría estudiada ("Tecnología", "Negocios", "Vida Diaria")
    val wordsLearned: Int, // Cantidad de verbos o palabras repasadas
    val totalQuestions: Int, // Total de preguntas evaluadas en el mini quiz
    val correctAnswers: Int, // Total de aciertos en el quiz
    val scorePercentage: Float, // Porcentaje de respuestas correctas (RF-006)
    val streakDays: Int, // Racha de días consecutivos activos (RF-007)
    val timestamp: Long = System.currentTimeMillis()
)
