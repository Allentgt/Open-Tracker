package com.indiancalorietracker.ui.screens.water

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indiancalorietracker.data.local.entity.WaterIntakeEntity
import com.indiancalorietracker.domain.repository.WaterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class WaterUiState(
    val isLoading: Boolean = false,
    val todayIntake: WaterIntakeEntity? = null,
    val goalMl: Int = 2000,
    val currentMl: Int = 0,
    val progress: Float = 0f,
    val error: String? = null
)

@HiltViewModel
class WaterViewModel @Inject constructor(
    private val waterRepository: WaterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WaterUiState())
    val uiState: StateFlow<WaterUiState> = _uiState.asStateFlow()

    init {
        loadTodayWater()
    }

    private fun loadTodayWater() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        viewModelScope.launch {
            waterRepository.getWaterIntakeForDate(today).collect { intake ->
                val currentMl = intake?.totalMl ?: 0
                val goalMl = intake?.goalMl ?: 2000
                _uiState.value = _uiState.value.copy(
                    todayIntake = intake,
                    currentMl = currentMl,
                    goalMl = goalMl,
                    progress = (currentMl.toFloat() / goalMl).coerceAtMost(1f),
                    isLoading = false
                )
            }
        }
    }

    fun addWater(amountMl: Int) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        viewModelScope.launch {
            waterRepository.addWater(today, amountMl)
        }
    }

    fun setGoal(goalMl: Int) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        viewModelScope.launch {
            waterRepository.setGoal(today, goalMl)
        }
    }
}