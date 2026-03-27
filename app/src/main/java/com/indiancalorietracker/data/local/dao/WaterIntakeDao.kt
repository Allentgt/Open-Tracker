package com.indiancalorietracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.indiancalorietracker.data.local.entity.WaterIntakeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterIntakeDao {
    @Query("SELECT * FROM water_intake WHERE date = :date")
    fun getWaterIntakeForDate(date: String): Flow<WaterIntakeEntity?>
    
    @Query("SELECT * FROM water_intake WHERE date = :date")
    suspend fun getWaterIntakeForDateOnce(date: String): WaterIntakeEntity?
    
    @Query("SELECT * FROM water_intake WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getWaterIntakeForRange(startDate: String, endDate: String): Flow<List<WaterIntakeEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(waterIntake: WaterIntakeEntity)
    
    @Update
    suspend fun update(waterIntake: WaterIntakeEntity)
    
    @Query("UPDATE water_intake SET totalMl = totalMl + :amount, lastUpdated = :timestamp WHERE date = :date")
    suspend fun addWater(date: String, amount: Int, timestamp: Long = System.currentTimeMillis())
}