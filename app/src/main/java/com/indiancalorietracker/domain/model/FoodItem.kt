package com.indiancalorietracker.domain.model

data class FoodItem(
    val id: Long = 0,
    val name: String,
    val nameHindi: String? = null,
    val category: String,
    val caloriesPerServing: Int,
    val servingSize: Float,
    val servingUnit: String,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val fiber: Float = 0f,
    val isIndian: Boolean = true
)
