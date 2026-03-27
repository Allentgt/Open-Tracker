package com.indiancalorietracker.domain.model

data class MealLog(
    val id: Long = 0,
    val foodItem: FoodItem,
    val date: Long,
    val mealType: MealType,
    val servings: Float,
    val totalCalories: Int,
    val timestamp: Long = System.currentTimeMillis()
)

enum class MealType(val displayName: String) {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    SNACKS("Snacks")
}
