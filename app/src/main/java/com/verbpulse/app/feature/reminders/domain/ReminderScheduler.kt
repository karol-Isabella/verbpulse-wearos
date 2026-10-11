package com.verbpulse.app.feature.reminders.domain

/**
 * Interfaz de dominio para programar y controlar los recordatorios periódicos de verbos en Wear OS.
 */
interface ReminderScheduler {
    /**
     * Programa recordatorios recurrentes cada [intervalHours] horas con WorkManager.
     */
    fun schedulePeriodicReminders(intervalHours: Int)

    /**
     * Dispara un recordatorio inmediato (para pruebas o demostración en el reloj).
     */
    fun triggerImmediateReminder()

    /**
     * Cancela cualquier recordatorio activo previamente programado.
     */
    fun cancelReminders()
}
