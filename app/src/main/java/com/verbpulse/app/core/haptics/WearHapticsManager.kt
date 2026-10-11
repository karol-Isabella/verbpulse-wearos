package com.verbpulse.app.core.haptics

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Gestor de vibración háptica especializado para relojes Wear OS.
 * Implementa patrones de retroalimentación optimizados para la muñeca con bajo impacto en batería.
 */
@Singleton
class WearHapticsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    /**
     * Patrón háptico de recordatorio periódico para Wear OS.
     * Genera un pulso doble distintivo y no intrusivo en la muñeca.
     */
    fun playReminderPulse() {
        val vib = vibrator ?: return
        if (!vib.hasVibrator()) return

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Pulso doble: esperar 0ms, vibrar 100ms, pausa 80ms, vibrar 160ms
                val timings = longArrayOf(0, 100, 80, 160)
                val amplitudes = intArrayOf(0, 180, 0, 255)
                val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                vib.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(longArrayOf(0, 100, 80, 160), -1)
            }
        } catch (e: Exception) {
            Timber.e(e, "Error al reproducir vibración de recordatorio")
        }
    }

    /**
     * Pulso háptico sutil para interacciones de interfaz (1 toque).
     */
    fun playSubtleClick() {
        val vib = vibrator ?: return
        if (!vib.hasVibrator()) return

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vib.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(40)
            }
        } catch (e: Exception) {
            Timber.e(e, "Error al reproducir click háptico")
        }
    }

    /**
     * Pulso háptico de acierto (ej. respuesta correcta en quiz).
     */
    fun playSuccessPulse() {
        val vib = vibrator ?: return
        if (!vib.hasVibrator()) return

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timings = longArrayOf(0, 60, 50, 90)
                val amplitudes = intArrayOf(0, 160, 0, 220)
                vib.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(longArrayOf(0, 60, 50, 90), -1)
            }
        } catch (e: Exception) {
            Timber.e(e, "Error al reproducir vibración de éxito")
        }
    }
}
