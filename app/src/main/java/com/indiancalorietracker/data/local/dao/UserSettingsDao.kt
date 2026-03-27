package com.indiancalorietracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.indiancalorietracker.data.local.entity.UserSettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSettingsDao {
    @Query("SELECT * FROM user_settings WHERE id = 1")
    fun getUserSettings(): Flow<UserSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(settings: UserSettingsEntity)

    @Query("UPDATE user_settings SET dailyCalorieGoal = :goal WHERE id = 1")
    suspend fun updateCalorieGoal(goal: Int)

    @Query("UPDATE user_settings SET userName = :name WHERE id = 1")
    suspend fun updateUserName(name: String)
}
