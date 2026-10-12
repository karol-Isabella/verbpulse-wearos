package com.verbpulse.app.feature.reminders.data

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.verbpulse.app.feature.reminders.domain.ReminderScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación de [ReminderScheduler] utilizando Android WorkManager.
 * Cumple con RNF-003 asegurando un consumo mínimo de batería gracias a la ejecución diferible y por lotes del sistema.
 */
@Singleton
class WorkManagerReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) : ReminderScheduler {

    companion object {
        const val PERIODIC_WORK_NAME = "verbpulse_periodic_reminder"
        const val IMMEDIATE_WORK_NAME = "verbpulse_immediate_reminder"
    }

    private val workManager: WorkManager by lazy {
        WorkManager.getInstance(context)
    }

    override fun schedulePeriodicReminders(intervalHours: Int) {
        val safeHours = intervalHours.coerceIn(1, 24).toLong()
        Timber.d("Programando recordatorios periódicos de VerbPulse cada %d horas", safeHours)

        val constraints = androidx.work.Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val periodicWorkRequest = PeriodicWorkRequestBuilder<VerbReminderWorker>(
            safeHours,
            TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            PERIODIC_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            periodicWorkRequest
        )
    }

    override fun triggerImmediateReminder() {
        Timber.d("Disparando recordatorio inmediato para demostración")
        val immediateWorkRequest = OneTimeWorkRequestBuilder<VerbReminderWorker>().build()

        workManager.enqueueUniqueWork(
            IMMEDIATE_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            immediateWorkRequest
        )
    }

    override fun cancelReminders() {
        Timber.d("Cancelando recordatorios activos de VerbPulse")
        workManager.cancelUniqueWork(PERIODIC_WORK_NAME)
    }
}
