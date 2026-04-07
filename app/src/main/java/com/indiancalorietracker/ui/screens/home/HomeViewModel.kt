package com.indiancalorietracker.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indiancalorietracker.domain.model.DailySummary
import com.indiancalorietracker.domain.model.MealLog
import com.indiancalorietracker.domain.model.MealType
import com.indiancalorietracker.domain.repository.MealRepository
import com.indiancalorietracker.domain.repository.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class HomeUiState(
    val dailySummary: DailySummary? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mealRepository: MealRepository,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<HomeEvent>()
    val events: SharedFlow<HomeEvent> = _events.asSharedFlow()

    init {
        loadTodayData()
    }

    private fun loadTodayData() {
        viewModelScope.launch {
            val today = getStartOfDay(System.currentTimeMillis())
            
            combine(
                mealRepository.getMealLogsByDate(today),
                mealRepository.getTotalCaloriesForDate(today),
                userSettingsRepository.getUserSettings()
            ) { meals, calories, settings ->
                HomeUiState(
                    dailySummary = DailySummary(
                        date = today,
                        totalCalories = calories,
                        calorieGoal = settings.dailyCalorieGoal,
                        meals = meals
                    ),
                    isLoading = false
                )
            }.collect { state ->
                _uiState.update { state }
            }
        }
    }

    fun getMealsByType(mealType: MealType): List<MealLog> {
        return _uiState.value.dailySummary?.meals?.filter { it.mealType == mealType } ?: emptyList()
    }

    fun deleteMeal(mealId: Long) {
        viewModelScope.launch {
            try {
                mealRepository.deleteMealLog(mealId)
                _events.emit(HomeEvent.MealDeleted)
            } catch (e: Exception) {
                _events.emit(HomeEvent.Error(e.message ?: "Failed to delete meal"))
            }
        }
    }

    private fun getStartOfDay(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
