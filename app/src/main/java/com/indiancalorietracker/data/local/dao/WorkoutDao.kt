package com.indiancalorietracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.indiancalorietracker.data.local.entity.WorkoutExerciseEntity
import com.indiancalorietracker.data.local.entity.WorkoutSessionEntity
import com.indiancalorietracker.data.local.entity.WorkoutSetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    // Session operations
    @Query("SELECT * FROM workout_sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<WorkoutSessionEntity>>
    
    @Query("SELECT * FROM workout_sessions WHERE id = :id")
    suspend fun getSessionById(id: Long): WorkoutSessionEntity?
    
    @Query("SELECT * FROM workout_sessions WHERE endTime IS NULL ORDER BY startTime DESC LIMIT 1")
    suspend fun getActiveSession(): WorkoutSessionEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: WorkoutSessionEntity): Long
    
    @Update
    suspend fun updateSession(session: WorkoutSessionEntity)
    
    @Delete
    suspend fun deleteSession(session: WorkoutSessionEntity)
    
    // Workout Exercise operations
    @Query("SELECT * FROM workout_exercises WHERE sessionId = :sessionId ORDER BY orderIndex")
    fun getExercisesForSession(sessionId: Long): Flow<List<WorkoutExerciseEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutExercise(workoutExercise: WorkoutExerciseEntity): Long
    
    @Delete
    suspend fun deleteWorkoutExercise(workoutExercise: WorkoutExerciseEntity)
    
    // Set operations
    @Query("SELECT * FROM workout_sets WHERE workoutExerciseId = :workoutExerciseId ORDER BY setNumber")
    fun getSetsForExercise(workoutExerciseId: Long): Flow<List<WorkoutSetEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(set: WorkoutSetEntity): Long
    
    @Update
    suspend fun updateSet(set: WorkoutSetEntity)
    
    @Delete
    suspend fun deleteSet(set: WorkoutSetEntity)
}