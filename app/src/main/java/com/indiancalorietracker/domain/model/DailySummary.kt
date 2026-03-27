package com.indiancalorietracker.domain.model

data class DailySummary(
    val date: Long,
    val totalCalories: Int,
    val calorieGoal: Int,
    val meals: List<MealLog>
) {
    val remainingCalories: Int get() = calorieGoal - totalCalories
    val progressPercentage: Float get() = (totalCalories.toFloat() / calorieGoal).coerceIn(0f, 1.5f)
    val isOverGoal: Boolean get() = totalCalories > calorieGoal
}
