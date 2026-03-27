package com.indiancalorietracker.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indiancalorietracker.domain.model.UserSettings
import com.indiancalorietracker.domain.repository.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val settings: UserSettings = UserSettings(),
    val isEditing: Boolean = false,
    val calorieGoalInput: String = "",
    val isLoading: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            userSettingsRepository.getUserSettings().collect { settings ->
                _uiState.value = _uiState.value.copy(
                    settings = settings,
                    calorieGoalInput = settings.dailyCalorieGoal.toString(),
                    isLoading = false
                )
            }
        }
    }

    fun startEditing() {
        _uiState.value = _uiState.value.copy(
            isEditing = true,
            calorieGoalInput = _uiState.value.settings.dailyCalorieGoal.toString()
        )
    }

    fun cancelEditing() {
        _uiState.value = _uiState.value.copy(
            isEditing = false,
            calorieGoalInput = _uiState.value.settings.dailyCalorieGoal.toString()
        )
    }

    fun onCalorieGoalChange(value: String) {
        _uiState.value = _uiState.value.copy(calorieGoalInput = value)
    }

    fun saveSettings() {
        val newGoal = _uiState.value.calorieGoalInput.toIntOrNull() ?: return
        viewModelScope.launch {
            userSettingsRepository.updateCalorieGoal(newGoal)
            _uiState.value = _uiState.value.copy(isEditing = false)
        }
    }
}