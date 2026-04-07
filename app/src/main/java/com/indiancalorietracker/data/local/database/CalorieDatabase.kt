package com.indiancalorietracker.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.indiancalorietracker.data.local.dao.ExerciseDao
import com.indiancalorietracker.data.local.dao.FoodItemDao
import com.indiancalorietracker.data.local.dao.MealLogDao
import com.indiancalorietracker.data.local.dao.UserSettingsDao
import com.indiancalorietracker.data.local.dao.WaterIntakeDao
import com.indiancalorietracker.data.local.dao.WorkoutDao
import com.indiancalorietracker.data.local.entity.ExerciseEntity
import com.indiancalorietracker.data.local.entity.FoodItemEntity
import com.indiancalorietracker.data.local.entity.MealLogEntity
import com.indiancalorietracker.data.local.entity.UserSettingsEntity
import com.indiancalorietracker.data.local.entity.WaterIntakeEntity
import com.indiancalorietracker.data.local.entity.WorkoutExerciseEntity
import com.indiancalorietracker.data.local.entity.WorkoutSessionEntity
import com.indiancalorietracker.data.local.entity.WorkoutSetEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        FoodItemEntity::class, 
        MealLogEntity::class, 
        UserSettingsEntity::class,
        WorkoutSessionEntity::class,
        ExerciseEntity::class,
        WorkoutExerciseEntity::class,
        WorkoutSetEntity::class,
        WaterIntakeEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class CalorieDatabase : RoomDatabase() {
    abstract fun foodItemDao(): FoodItemDao
    abstract fun mealLogDao(): MealLogDao
    abstract fun userSettingsDao(): UserSettingsDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun waterIntakeDao(): WaterIntakeDao

    companion object {
        const val DATABASE_NAME = "calorie_database"
    }
}
