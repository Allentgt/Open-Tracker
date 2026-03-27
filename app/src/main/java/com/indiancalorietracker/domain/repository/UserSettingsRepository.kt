package com.indiancalorietracker.domain.repository

import com.indiancalorietracker.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface UserSettingsRepository {
    fun getUserSettings(): Flow<UserSettings>
    suspend fun updateCalorieGoal(goal: Int)
    suspend fun updateUserName(name: String)
}
