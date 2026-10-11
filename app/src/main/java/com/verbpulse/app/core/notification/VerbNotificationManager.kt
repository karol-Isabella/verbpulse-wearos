package com.verbpulse.app.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.verbpulse.app.R
import com.verbpulse.app.feature.learning.data.IrregularVerb
import com.verbpulse.app.presentation.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Gestor de notificaciones especializado para Wear OS.
 * Presenta microlecciones interactivas accesibles en 1 toque.
 */
@Singleton
class VerbNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val CHANNEL_ID = "verbpulse_reminders_channel"
        const val CHANNEL_NAME = "Recordatorios de Vocabulario"
        const val NOTIFICATION_ID = 1001

        const val EXTRA_START_LEARNING = "extra_start_learning"
        const val EXTRA_CATEGORY = "extra_category"
        const val EXTRA_VERB = "extra_verb"
    }

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Recordatorios de verbos y microlecciones periódicas de VerbPulse"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 100, 80, 160)
                setShowBadge(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showVerbReminder(verb: IrregularVerb) {
        try {
            // Intent para abrir MainActivity directamente en Flashcard (RNF-005: 1 toque)
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(EXTRA_START_LEARNING, true)
                putExtra(EXTRA_CATEGORY, verb.category)
                putExtra(EXTRA_VERB, verb.present)
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val bigTextStyle = NotificationCompat.BigTextStyle()
                .setBigContentTitle("Verbo: ${verb.present.uppercase()}")
                .bigText(
                    "• Past: ${verb.past}\n" +
                    "• Participle: ${verb.participle}\n" +
                    "• Significado: ${verb.translationEs}\n" +
                    "• Categoría: ${verb.category}"
                )

            val studyAction = NotificationCompat.Action.Builder(
                R.drawable.splash_icon,
                "Repasar",
                pendingIntent
            ).build()

            val wearableExtender = NotificationCompat.WearableExtender()
                .addAction(studyAction)
                .setContentAction(0) // Primer acción al tocar en Wear OS

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.splash_icon)
                .setContentTitle("VerbPulse · ${verb.present.uppercase()}")
                .setContentText("${verb.past} · ${verb.participle} (${verb.translationEs})")
                .setStyle(bigTextStyle)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .addAction(studyAction)
                .extend(wearableExtender)
                .build()

            notificationManager.notify(NOTIFICATION_ID, notification)
        } catch (e: Exception) {
            Timber.e(e, "Error al mostrar notificación de recordatorio en Wear OS")
        }
    }
}
