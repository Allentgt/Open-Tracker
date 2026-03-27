package com.indiancalorietracker.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indiancalorietracker.domain.model.FoodItem
import com.indiancalorietracker.domain.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val searchResults: List<FoodItem> = emptyList(),
    val allCategories: List<String> = emptyList(),
    val selectedCategory: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val foodRepository: FoodRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
        loadAllFoods()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            foodRepository.getAllFoodItems().collect { foods ->
                val categories = foods.map { it.category }.distinct().sorted()
                _uiState.value = _uiState.value.copy(allCategories = categories)
            }
        }
    }

    private fun loadAllFoods() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            foodRepository.getAllFoodItems().collect { foods ->
                _uiState.value = _uiState.value.copy(
                    searchResults = foods,
                    isLoading = false
                )
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
        if (query.isBlank()) {
            loadAllFoods()
        } else {
            searchFoods(query)
        }
    }

    private fun searchFoods(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            foodRepository.searchFoodItems(query).collect { foods ->
                _uiState.value = _uiState.value.copy(
                    searchResults = foods,
                    isLoading = false
                )
            }
        }
    }

    fun onCategorySelected(category: String?) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        if (category == null) {
            loadAllFoods()
        } else {
            filterByCategory(category)
        }
    }

    private fun filterByCategory(category: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            foodRepository.getFoodItemsByCategory(category).collect { foods ->
                _uiState.value = _uiState.value.copy(
                    searchResults = foods,
                    isLoading = false
                )
            }
        }
    }
}
