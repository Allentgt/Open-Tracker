package com.indiancalorietracker.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indiancalorietracker.domain.model.DailySummary
import com.indiancalorietracker.domain.repository.MealRepository
import com.indiancalorietracker.domain.repository.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class HistoryUiState(
    val dailySummaries: List<DailySummary> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val mealRepository: MealRepository,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            val endDate = getStartOfDay(calendar.timeInMillis)
            calendar.add(Calendar.DAY_OF_YEAR, -30)
            val startDate = getStartOfDay(calendar.timeInMillis)

            combine(
                mealRepository.getMealLogsBetweenDates(startDate, endDate),
                userSettingsRepository.getUserSettings()
            ) { meals, settings ->
                val groupedByDate = meals.groupBy { it.date }
                groupedByDate.map { (date, dayMeals) ->
                    DailySummary(
                        date = date,
                        totalCalories = dayMeals.sumOf { it.totalCalories },
                        calorieGoal = settings.dailyCalorieGoal,
                        meals = dayMeals
                    )
                }.sortedByDescending { it.date }
            }.collect { summaries ->
                _uiState.value = HistoryUiState(
                    dailySummaries = summaries,
                    isLoading = false
                )
            }
        }
    }

    fun deleteMeal(mealId: Long) {
        viewModelScope.launch {
            mealRepository.deleteMealLog(mealId)
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
