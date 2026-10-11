package com.verbpulse.app.feature.reminders.di

import com.verbpulse.app.feature.reminders.data.WorkManagerReminderScheduler
import com.verbpulse.app.feature.reminders.domain.ReminderScheduler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemindersModule {

    @Binds
    @Singleton
    abstract fun bindReminderScheduler(
        impl: WorkManagerReminderScheduler
    ): ReminderScheduler
}
