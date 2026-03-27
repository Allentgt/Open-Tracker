package com.indiancalorietracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_intake")
data class WaterIntakeEntity(
    @PrimaryKey
    val date: String, // Format: "yyyy-MM-dd"
    val totalMl: Int = 0,
    val goalMl: Int = 2000,
    val lastUpdated: Long = System.currentTimeMillis()
)