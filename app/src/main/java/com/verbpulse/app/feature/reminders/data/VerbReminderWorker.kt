package com.verbpulse.app.feature.reminders.data

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.verbpulse.app.core.data.preferences.UserPreferencesRepository
import com.verbpulse.app.core.haptics.WearHapticsManager
import com.verbpulse.app.core.notification.VerbNotificationManager
import com.verbpulse.app.feature.learning.data.VerbsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import timber.log.Timber

/**
 * Worker en segundo plano para procesar microlecciones periódicas.
 * Se ejecuta respetando la frecuencia configurada por el usuario, emite vibración háptica
 * y publica la notificación con interacción de 1 toque.
 */
@HiltWorker
class VerbReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val verbsRepository: VerbsRepository,
    private val notificationManager: VerbNotificationManager,
    private val hapticsManager: WearHapticsManager
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            Timber.d("Ejecutando VerbReminderWorker para Wear OS...")

            // Obtener preferencias activas del usuario (categoría configurada)
            val prefs = userPreferencesRepository.userPreferences.first()
            val category = prefs.selectedCategory

            // Obtener verbos correspondientes a la categoría seleccionada
            val categoryVerbs = verbsRepository.getVerbsByCategory(category)
            val verb = if (categoryVerbs.isNotEmpty()) {
                categoryVerbs.random()
            } else {
                verbsRepository.getRandomVerb()
            }

            // 1. Emitir pulso háptico en la muñeca (RF-002)
            hapticsManager.playReminderPulse()

            // 2. Mostrar notificación interactiva en Wear OS
            notificationManager.showVerbReminder(verb)

            Timber.i("Recordatorio periódico enviado con éxito para el verbo: %s", verb.present)
            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "Error al ejecutar VerbReminderWorker")
            Result.retry()
        }
    }
}
