package com.indiancalorietracker.data.repository

import com.indiancalorietracker.data.local.dao.UserSettingsDao
import com.indiancalorietracker.data.local.entity.UserSettingsEntity
import com.indiancalorietracker.domain.model.UserSettings
import com.indiancalorietracker.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSettingsRepositoryImpl @Inject constructor(
    private val userSettingsDao: UserSettingsDao
) : UserSettingsRepository {

    override fun getUserSettings(): Flow<UserSettings> {
        return userSettingsDao.getUserSettings().map { entity ->
            entity?.let {
                UserSettings(
                    dailyCalorieGoal = it.dailyCalorieGoal,
                    userName = it.userName
                )
            } ?: UserSettings()
        }
    }

    override suspend fun updateCalorieGoal(goal: Int) {
        userSettingsDao.updateCalorieGoal(goal)
    }

    override suspend fun updateUserName(name: String) {
        userSettingsDao.updateUserName(name)
    }
}
