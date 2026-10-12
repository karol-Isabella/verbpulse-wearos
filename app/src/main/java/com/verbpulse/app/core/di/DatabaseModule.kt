package com.verbpulse.app.core.di

import android.content.Context
import androidx.room.Room
import com.verbpulse.app.feature.progress.data.ProgressDao
import com.verbpulse.app.feature.progress.data.ProgressDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideProgressDatabase(
        @ApplicationContext context: Context
    ): ProgressDatabase = Room.databaseBuilder(
        context,
        ProgressDatabase::class.java,
        "verbpulse_database"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideProgressDao(database: ProgressDatabase): ProgressDao = database.progressDao()
}
