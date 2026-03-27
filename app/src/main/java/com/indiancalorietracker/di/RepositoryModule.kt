package com.indiancalorietracker.di

import com.indiancalorietracker.data.repository.AIAssistantRepositoryImpl
import com.indiancalorietracker.data.repository.ExerciseRepositoryImpl
import com.indiancalorietracker.data.repository.FoodRepositoryImpl
import com.indiancalorietracker.data.repository.MealRepositoryImpl
import com.indiancalorietracker.data.repository.UserSettingsRepositoryImpl
import com.indiancalorietracker.data.repository.WaterRepositoryImpl
import com.indiancalorietracker.data.repository.WorkoutRepositoryImpl
import com.indiancalorietracker.domain.repository.AIAssistantRepository
import com.indiancalorietracker.domain.repository.ExerciseRepository
import com.indiancalorietracker.domain.repository.FoodRepository
import com.indiancalorietracker.domain.repository.MealRepository
import com.indiancalorietracker.domain.repository.UserSettingsRepository
import com.indiancalorietracker.domain.repository.WaterRepository
import com.indiancalorietracker.domain.repository.WorkoutRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFoodRepository(impl: FoodRepositoryImpl): FoodRepository

    @Binds
    @Singleton
    abstract fun bindMealRepository(impl: MealRepositoryImpl): MealRepository

    @Binds
    @Singleton
    abstract fun bindUserSettingsRepository(impl: UserSettingsRepositoryImpl): UserSettingsRepository

    @Binds
    @Singleton
    abstract fun bindWorkoutRepository(impl: WorkoutRepositoryImpl): WorkoutRepository

    @Binds
    @Singleton
    abstract fun bindExerciseRepository(impl: ExerciseRepositoryImpl): ExerciseRepository

    @Binds
    @Singleton
    abstract fun bindWaterRepository(impl: WaterRepositoryImpl): WaterRepository

    @Binds
    @Singleton
    abstract fun bindAIAssistantRepository(impl: AIAssistantRepositoryImpl): AIAssistantRepository
}
