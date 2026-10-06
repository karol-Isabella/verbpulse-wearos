package com.verbpulse.app.core.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        private val LANGUAGE = stringPreferencesKey("language")
        private val DAILY_GOAL = intPreferencesKey("daily_goal")
        private val FREQUENCY = intPreferencesKey("frequency")
    }

    val userPreferences: Flow<UserPreferences> = dataStore.data.map { preferences ->
        UserPreferences(
            isDarkMode = preferences[IS_DARK_MODE] ?: false,
            language = preferences[LANGUAGE] ?: "ES",
            dailyGoal = preferences[DAILY_GOAL] ?: 5,
            frequency = preferences[FREQUENCY] ?: 1
        )
    }

    suspend fun updateIsDarkMode(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = isDarkMode
        }
    }

    suspend fun updateLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE] = language
        }
    }

    suspend fun updateDailyGoal(goal: Int) {
        dataStore.edit { preferences ->
            preferences[DAILY_GOAL] = goal
        }
    }

    suspend fun updateFrequency(frequency: Int) {
        dataStore.edit { preferences ->
            preferences[FREQUENCY] = frequency
        }
    }
}
