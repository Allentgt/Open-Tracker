package com.indiancalorietracker.ui.screens.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indiancalorietracker.domain.model.FoodItem
import com.indiancalorietracker.domain.model.MealLog
import com.indiancalorietracker.domain.model.MealType
import com.indiancalorietracker.domain.repository.FoodRepository
import com.indiancalorietracker.domain.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class LogMealUiState(
    val searchQuery: String = "",
    val searchResults: List<FoodItem> = emptyList(),
    val selectedFood: FoodItem? = null,
    val selectedMealType: MealType = MealType.LUNCH,
    val servings: Float = 1f,
    val isSearching: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class LogMealViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val mealRepository: MealRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LogMealUiState())
    val uiState: StateFlow<LogMealUiState> = _uiState.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        if (query.length >= 2) {
            searchFoods(query)
        } else {
            _uiState.update { it.copy(searchResults = emptyList()) }
        }
    }

    private fun searchFoods(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSearching = true) }
            foodRepository.searchFoodItems(query).collect { foods ->
                _uiState.update { it.copy(
                    searchResults = foods,
                    isSearching = false
                ) }
            }
        }
    }

    fun onFoodSelected(food: FoodItem) {
        _uiState.update { it.copy(
            selectedFood = food,
            searchResults = emptyList(),
            searchQuery = ""
        ) }
    }

    fun onMealTypeSelected(mealType: MealType) {
        _uiState.update { it.copy(selectedMealType = mealType) }
    }

    fun onServingsChange(servings: Float) {
        _uiState.update { it.copy(servings = servings.coerceAtLeast(0.5f)) }
    }

    fun saveMeal() {
        val state = _uiState.value
        val food = state.selectedFood ?: return

        viewModelScope.launch {
            try {
                val today = getStartOfDay(System.currentTimeMillis())
                val mealLog = MealLog(
                    foodItem = food,
                    date = today,
                    mealType = state.selectedMealType,
                    servings = state.servings,
                    totalCalories = (food.caloriesPerServing * state.servings).toInt()
                )
                mealRepository.logMeal(mealLog)
                _uiState.update { LogMealUiState(isSaved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    fun resetState() {
        _uiState.update { LogMealUiState() }
    }

    fun clearSelection() {
        _uiState.update { it.copy(selectedFood = null, servings = 1f) }
    }

    private fun getStartOfDay(timestamp: Long): Long {
        return Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }
}
