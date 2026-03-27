package com.indiancalorietracker.domain.repository

import com.indiancalorietracker.data.local.entity.ExerciseEntity
import com.indiancalorietracker.data.local.entity.WorkoutExerciseEntity
import com.indiancalorietracker.data.local.entity.WorkoutSessionEntity
import com.indiancalorietracker.data.local.entity.WorkoutSetEntity
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getAllSessions(): Flow<List<WorkoutSessionEntity>>
    suspend fun getSessionById(id: Long): WorkoutSessionEntity?
    suspend fun getActiveSession(): WorkoutSessionEntity?
    suspend fun createSession(session: WorkoutSessionEntity): Long
    suspend fun updateSession(session: WorkoutSessionEntity)
    suspend fun deleteSession(session: WorkoutSessionEntity)
    
    fun getExercisesForSession(sessionId: Long): Flow<List<WorkoutExerciseEntity>>
    suspend fun addExerciseToSession(workoutExercise: WorkoutExerciseEntity): Long
    suspend fun removeExerciseFromSession(workoutExercise: WorkoutExerciseEntity)
    
    fun getSetsForExercise(workoutExerciseId: Long): Flow<List<WorkoutSetEntity>>
    suspend fun addSet(set: WorkoutSetEntity): Long
    suspend fun updateSet(set: WorkoutSetEntity)
    suspend fun deleteSet(set: WorkoutSetEntity)
}

interface ExerciseRepository {
    fun getAllExercises(): Flow<List<ExerciseEntity>>
    fun getExercisesByCategory(category: String): Flow<List<ExerciseEntity>>
    fun searchExercises(query: String): Flow<List<ExerciseEntity>>
    suspend fun getExerciseById(id: Long): ExerciseEntity?
    suspend fun insertExercise(exercise: ExerciseEntity): Long
    suspend fun getCount(): Int
}