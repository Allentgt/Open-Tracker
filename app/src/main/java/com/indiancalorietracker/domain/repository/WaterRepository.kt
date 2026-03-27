package com.indiancalorietracker.domain.repository

import com.indiancalorietracker.data.local.entity.WaterIntakeEntity
import kotlinx.coroutines.flow.Flow

interface WaterRepository {
    fun getWaterIntakeForDate(date: String): Flow<WaterIntakeEntity?>
    suspend fun getWaterIntakeForDateOnce(date: String): WaterIntakeEntity?
    fun getWaterIntakeForRange(startDate: String, endDate: String): Flow<List<WaterIntakeEntity>>
    suspend fun addWater(date: String, amount: Int)
    suspend fun setGoal(date: String, goalMl: Int)
}