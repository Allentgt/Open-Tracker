package com.indiancalorietracker.ui.screens.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indiancalorietracker.data.local.entity.ExerciseEntity
import com.indiancalorietracker.data.local.entity.WorkoutExerciseEntity
import com.indiancalorietracker.data.local.entity.WorkoutSessionEntity
import com.indiancalorietracker.data.local.entity.WorkoutSetEntity
import com.indiancalorietracker.domain.repository.ExerciseRepository
import com.indiancalorietracker.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WorkoutUiState(
    val isLoading: Boolean = false,
    val activeSession: WorkoutSessionEntity? = null,
    val sessions: List<WorkoutSessionEntity> = emptyList(),
    val exercisesInSession: List<WorkoutExerciseEntity> = emptyList(),
    val availableExercises: List<ExerciseEntity> = emptyList(),
    val setsForExercise: Map<Long, List<WorkoutSetEntity>> = emptyMap(),
    val error: String? = null
)

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutUiState())
    val uiState: StateFlow<WorkoutUiState> = _uiState.asStateFlow()

    init {
        loadSessions()
        loadExercises()
        checkActiveSession()
    }

    private fun loadSessions() {
        viewModelScope.launch {
            workoutRepository.getAllSessions().collect { sessions ->
                _uiState.value = _uiState.value.copy(sessions = sessions)
            }
        }
    }

    private fun loadExercises() {
        viewModelScope.launch {
            exerciseRepository.getAllExercises().collect { exercises ->
                if (exercises.isEmpty()) {
                    // Seed default exercises
                    seedDefaultExercises()
                } else {
                    _uiState.value = _uiState.value.copy(availableExercises = exercises)
                }
            }
        }
    }

    private suspend fun seedDefaultExercises() {
        val defaultExercises = listOf(
            ExerciseEntity(name = "Bench Press", category = "Strength", targetMuscles = "Chest,Triceps", description = "Chest exercise"),
            ExerciseEntity(name = "Squat", category = "Strength", targetMuscles = "Quads,Glutes", description = "Leg exercise"),
            ExerciseEntity(name = "Deadlift", category = "Strength", targetMuscles = "Back,Glutes", description = "Full body"),
            ExerciseEntity(name = "Overhead Press", category = "Strength", targetMuscles = "Shoulders,Triceps", description = "Shoulder exercise"),
            ExerciseEntity(name = "Barbell Row", category = "Strength", targetMuscles = "Back,Biceps", description = "Back exercise"),
            ExerciseEntity(name = "Pull Up", category = "Strength", targetMuscles = "Back,Biceps", description = "Bodyweight back"),
            ExerciseEntity(name = "Dumbbell Curl", category = "Strength", targetMuscles = "Biceps", description = "Arm exercise"),
            ExerciseEntity(name = "Tricep Pushdown", category = "Strength", targetMuscles = "Triceps", description = "Arm exercise"),
            ExerciseEntity(name = "Leg Press", category = "Strength", targetMuscles = "Quads,Glutes", description = "Leg machine"),
            ExerciseEntity(name = "Lat Pulldown", category = "Strength", targetMuscles = "Back,Biceps", description = "Cable back"),
            ExerciseEntity(name = "Running", category = "Cardio", targetMuscles = "Heart,Legs", description = "Cardio"),
            ExerciseEntity(name = "Cycling", category = "Cardio", targetMuscles = "Heart,Legs", description = "Cardio"),
            ExerciseEntity(name = "Jump Rope", category = "Cardio", targetMuscles = "Heart,Legs", description = "Cardio"),
            ExerciseEntity(name = "Plank", category = "Core", targetMuscles = "Abs,Core", description = "Core stability"),
            ExerciseEntity(name = "Crunches", category = "Core", targetMuscles = "Abs", description = "Ab exercise"),
            ExerciseEntity(name = "Yoga Flow", category = "Flexibility", targetMuscles = "Full Body", description = "Stretching")
        )
        defaultExercises.forEach { exerciseRepository.insertExercise(it) }
    }

    private fun checkActiveSession() {
        viewModelScope.launch {
            val activeSession = workoutRepository.getActiveSession()
            _uiState.value = _uiState.value.copy(activeSession = activeSession)
            activeSession?.let { loadExercisesForSession(it.id) }
        }
    }

    private fun loadExercisesForSession(sessionId: Long) {
        viewModelScope.launch {
            workoutRepository.getExercisesForSession(sessionId).collect { exercises ->
                _uiState.value = _uiState.value.copy(exercisesInSession = exercises)
            }
        }
    }

    fun startNewWorkout() {
        viewModelScope.launch {
            val session = WorkoutSessionEntity(
                name = "Workout ${System.currentTimeMillis()}",
                startTime = System.currentTimeMillis()
            )
            val sessionId = workoutRepository.createSession(session)
            val createdSession = workoutRepository.getSessionById(sessionId)
            _uiState.value = _uiState.value.copy(activeSession = createdSession)
        }
    }

    fun addExerciseToWorkout(exerciseId: Long) {
        val sessionId = _uiState.value.activeSession?.id ?: return
        viewModelScope.launch {
            val workoutExercise = WorkoutExerciseEntity(
                sessionId = sessionId,
                exerciseId = exerciseId,
                orderIndex = _uiState.value.exercisesInSession.size
            )
            workoutRepository.addExerciseToSession(workoutExercise)
        }
    }

    fun addSetToExercise(workoutExerciseId: Long) {
        viewModelScope.launch {
            val currentSets = _uiState.value.setsForExercise[workoutExerciseId]?.size ?: 0
            val set = WorkoutSetEntity(
                workoutExerciseId = workoutExerciseId,
                setNumber = currentSets + 1
            )
            workoutRepository.addSet(set)
        }
    }

    fun updateSet(set: WorkoutSetEntity) {
        viewModelScope.launch {
            workoutRepository.updateSet(set.copy(isCompleted = true, completedAt = System.currentTimeMillis()))
        }
    }

    fun completeWorkout() {
        val session = _uiState.value.activeSession ?: return
        viewModelScope.launch {
            workoutRepository.updateSession(
                session.copy(endTime = System.currentTimeMillis())
            )
            _uiState.value = _uiState.value.copy(activeSession = null)
        }
    }

    fun deleteSession(session: WorkoutSessionEntity) {
        viewModelScope.launch {
            workoutRepository.deleteSession(session)
        }
    }
}