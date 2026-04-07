package com.indiancalorietracker.ui.screens.home

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * One-off UI events for Home screen
 */
sealed class HomeEvent {
    data class ShowSnackbar(val message: String) : HomeEvent()
    data object MealDeleted : HomeEvent()
    data class Error(val message: String) : HomeEvent()
}