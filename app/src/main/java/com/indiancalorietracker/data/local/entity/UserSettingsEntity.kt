package com.indiancalorietracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey
    val id: Int = 1,
    val dailyCalorieGoal: Int = 2000,
    val userName: String = ""
)
