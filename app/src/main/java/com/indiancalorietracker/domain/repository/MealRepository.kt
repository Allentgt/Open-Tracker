package com.indiancalorietracker.domain.repository

import com.indiancalorietracker.domain.model.MealLog
import kotlinx.coroutines.flow.Flow

interface MealRepository {
    fun getMealLogsByDate(date: Long): Flow<List<MealLog>>
    fun getMealLogsBetweenDates(startDate: Long, endDate: Long): Flow<List<MealLog>>
    fun getTotalCaloriesForDate(date: Long): Flow<Int>
    suspend fun logMeal(mealLog: MealLog): Long
    suspend fun deleteMealLog(id: Long)
}
