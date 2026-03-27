package com.indiancalorietracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_logs")
data class MealLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val foodItemId: Long,
    val date: Long,
    val mealType: String,
    val servings: Float,
    val totalCalories: Int,
    val timestamp: Long = System.currentTimeMillis()
)
