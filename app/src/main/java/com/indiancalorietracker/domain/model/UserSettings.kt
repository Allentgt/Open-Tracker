package com.indiancalorietracker.domain.model

data class UserSettings(
    val dailyCalorieGoal: Int = 2000,
    val userName: String = ""
)
