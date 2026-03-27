package com.indiancalorietracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_sessions")
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String = "Workout",
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long? = null,
    val notes: String = ""
)