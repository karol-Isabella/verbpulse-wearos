package com.verbpulse.app.feature.progress.data

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Base de datos Room para la persistencia local de estadísticas y progreso en VerbPulse.
 */
@Database(
    entities = [DailyProgressEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ProgressDatabase : RoomDatabase() {
    abstract fun progressDao(): ProgressDao
}
