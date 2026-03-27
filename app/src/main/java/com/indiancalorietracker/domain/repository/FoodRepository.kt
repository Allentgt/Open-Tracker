package com.indiancalorietracker.domain.repository

import com.indiancalorietracker.domain.model.FoodItem
import kotlinx.coroutines.flow.Flow

interface FoodRepository {
    fun getAllFoodItems(): Flow<List<FoodItem>>
    fun searchFoodItems(query: String): Flow<List<FoodItem>>
    fun getFoodItemsByCategory(category: String): Flow<List<FoodItem>>
    suspend fun getFoodItemById(id: Long): FoodItem?
}
