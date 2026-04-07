package com.indiancalorietracker.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.indiancalorietracker.data.local.dao.ExerciseDao
import com.indiancalorietracker.data.local.dao.FoodItemDao
import com.indiancalorietracker.data.local.dao.MealLogDao
import com.indiancalorietracker.data.local.dao.UserSettingsDao
import com.indiancalorietracker.data.local.dao.WaterIntakeDao
import com.indiancalorietracker.data.local.dao.WorkoutDao
import com.indiancalorietracker.data.local.database.CalorieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CalorieDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CalorieDatabase::class.java,
            CalorieDatabase.DATABASE_NAME
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Data seeding is handled by repositories on first access
                }
            })
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideFoodItemDao(database: CalorieDatabase): FoodItemDao {
        return database.foodItemDao()
    }

    @Provides
    fun provideMealLogDao(database: CalorieDatabase): MealLogDao {
        return database.mealLogDao()
    }

    @Provides
    fun provideUserSettingsDao(database: CalorieDatabase): UserSettingsDao {
        return database.userSettingsDao()
    }

    @Provides
    fun provideWorkoutDao(database: CalorieDatabase): WorkoutDao {
        return database.workoutDao()
    }

    @Provides
    fun provideExerciseDao(database: CalorieDatabase): ExerciseDao {
        return database.exerciseDao()
    }

    @Provides
    fun provideWaterIntakeDao(database: CalorieDatabase): WaterIntakeDao {
        return database.waterIntakeDao()
    }
}
