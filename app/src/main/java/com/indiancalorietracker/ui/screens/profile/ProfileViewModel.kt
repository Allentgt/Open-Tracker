package com.indiancalorietracker.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indiancalorietracker.domain.model.UserSettings
import com.indiancalorietracker.domain.repository.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val settings: UserSettings = UserSettings(),
    val isEditing: Boolean = false,
    val calorieGoalInput: String = ""
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            userSettingsRepository.getUserSettings().collect { settings ->
                _uiState.update { it.copy(
                    settings = settings,
                    calorieGoalInput = settings.dailyCalorieGoal.toString()
                ) }
            }
        }
    }

    fun startEditing() {
        _uiState.update { it.copy(
            isEditing = true,
            calorieGoalInput = it.settings.dailyCalorieGoal.toString()
        ) }
    }

    fun onCalorieGoalChange(value: String) {
        _uiState.update { it.copy(calorieGoalInput = value) }
    }

    fun saveSettings() {
        val goal = _uiState.value.calorieGoalInput.toIntOrNull()
        if (goal != null && goal in 500..10000) {
            viewModelScope.launch {
                userSettingsRepository.updateCalorieGoal(goal)
                _uiState.update { it.copy(isEditing = false) }
            }
        }
    }

    fun cancelEditing() {
        _uiState.update { it.copy(
            isEditing = false,
            calorieGoalInput = it.settings.dailyCalorieGoal.toString()
        ) }
    }
}
