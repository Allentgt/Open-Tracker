package com.indiancalorietracker.data.repository

import com.indiancalorietracker.data.local.dao.ExerciseDao
import com.indiancalorietracker.data.local.dao.WorkoutDao
import com.indiancalorietracker.data.local.entity.ExerciseEntity
import com.indiancalorietracker.data.local.entity.WorkoutExerciseEntity
import com.indiancalorietracker.data.local.entity.WorkoutSessionEntity
import com.indiancalorietracker.data.local.entity.WorkoutSetEntity
import com.indiancalorietracker.domain.repository.ExerciseRepository
import com.indiancalorietracker.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val workoutDao: WorkoutDao
) : WorkoutRepository {

    override fun getAllSessions(): Flow<List<WorkoutSessionEntity>> {
        return workoutDao.getAllSessions()
    }

    override suspend fun getSessionById(id: Long): WorkoutSessionEntity? {
        return workoutDao.getSessionById(id)
    }

    override suspend fun getActiveSession(): WorkoutSessionEntity? {
        return workoutDao.getActiveSession()
    }

    override suspend fun createSession(session: WorkoutSessionEntity): Long {
        return workoutDao.insertSession(session)
    }

    override suspend fun updateSession(session: WorkoutSessionEntity) {
        workoutDao.updateSession(session)
    }

    override suspend fun deleteSession(session: WorkoutSessionEntity) {
        workoutDao.deleteSession(session)
    }

    override fun getExercisesForSession(sessionId: Long): Flow<List<WorkoutExerciseEntity>> {
        return workoutDao.getExercisesForSession(sessionId)
    }

    override suspend fun addExerciseToSession(workoutExercise: WorkoutExerciseEntity): Long {
        return workoutDao.insertWorkoutExercise(workoutExercise)
    }

    override suspend fun removeExerciseFromSession(workoutExercise: WorkoutExerciseEntity) {
        workoutDao.deleteWorkoutExercise(workoutExercise)
    }

    override fun getSetsForExercise(workoutExerciseId: Long): Flow<List<WorkoutSetEntity>> {
        return workoutDao.getSetsForExercise(workoutExerciseId)
    }

    override suspend fun addSet(set: WorkoutSetEntity): Long {
        return workoutDao.insertSet(set)
    }

    override suspend fun updateSet(set: WorkoutSetEntity) {
        workoutDao.updateSet(set)
    }

    override suspend fun deleteSet(set: WorkoutSetEntity) {
        workoutDao.deleteSet(set)
    }
}

class ExerciseRepositoryImpl @Inject constructor(
    private val exerciseDao: ExerciseDao
) : ExerciseRepository {

    override fun getAllExercises(): Flow<List<ExerciseEntity>> {
        return exerciseDao.getAllExercises()
    }

    override fun getExercisesByCategory(category: String): Flow<List<ExerciseEntity>> {
        return exerciseDao.getExercisesByCategory(category)
    }

    override fun searchExercises(query: String): Flow<List<ExerciseEntity>> {
        return exerciseDao.searchExercises(query)
    }

    override suspend fun getExerciseById(id: Long): ExerciseEntity? {
        return exerciseDao.getExerciseById(id)
    }

    override suspend fun insertExercise(exercise: ExerciseEntity): Long {
        return exerciseDao.insertExercise(exercise)
    }

    override suspend fun getCount(): Int {
        return exerciseDao.getCount()
    }
}