package com.indiancalorietracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.indiancalorietracker.data.local.entity.MealLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealLogDao {
    @Query("SELECT * FROM meal_logs WHERE date = :date ORDER BY timestamp DESC")
    fun getMealLogsByDate(date: Long): Flow<List<MealLogEntity>>

    @Query("SELECT * FROM meal_logs WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC, timestamp DESC")
    fun getMealLogsBetweenDates(startDate: Long, endDate: Long): Flow<List<MealLogEntity>>

    @Query("SELECT SUM(totalCalories) FROM meal_logs WHERE date = :date")
    fun getTotalCaloriesForDate(date: Long): Flow<Int?>

    @Query("SELECT SUM(totalCalories) FROM meal_logs WHERE date BETWEEN :startDate AND :endDate GROUP BY date")
    fun getDailyCaloriesBetweenDates(startDate: Long, endDate: Long): Flow<List<Int>>

    @Insert
    suspend fun insert(mealLog: MealLogEntity): Long

    @Delete
    suspend fun delete(mealLog: MealLogEntity)

    @Query("DELETE FROM meal_logs WHERE id = :id")
    suspend fun deleteById(id: Long)
}
