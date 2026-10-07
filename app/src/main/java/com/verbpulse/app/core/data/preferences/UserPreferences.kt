package com.verbpulse.app.core.data.preferences

data class UserPreferences(
    val isDarkMode: Boolean = false,
    val language: String = "ES",
    val dailyGoal: Int = 5,
    val frequency: Int = 1,
    val selectedCategory: String = "Tecnología"
)
