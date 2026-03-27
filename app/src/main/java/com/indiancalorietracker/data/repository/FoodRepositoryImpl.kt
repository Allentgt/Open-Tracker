package com.indiancalorietracker.data.repository

import com.indiancalorietracker.data.local.dao.FoodItemDao
import com.indiancalorietracker.data.local.entity.FoodItemEntity
import com.indiancalorietracker.domain.model.FoodItem
import com.indiancalorietracker.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepositoryImpl @Inject constructor(
    private val foodItemDao: FoodItemDao
) : FoodRepository {

    override fun getAllFoodItems(): Flow<List<FoodItem>> {
        return foodItemDao.getAllFoodItems().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun searchFoodItems(query: String): Flow<List<FoodItem>> {
        return foodItemDao.searchFoodItems(query).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getFoodItemsByCategory(category: String): Flow<List<FoodItem>> {
        return foodItemDao.getFoodItemsByCategory(category).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getFoodItemById(id: Long): FoodItem? {
        return foodItemDao.getFoodItemById(id)?.toDomainModel()
    }

    private fun FoodItemEntity.toDomainModel() = FoodItem(
        id = id,
        name = name,
        nameHindi = nameHindi,
        category = category,
        caloriesPerServing = caloriesPerServing,
        servingSize = servingSize,
        servingUnit = servingUnit,
        protein = protein,
        carbs = carbs,
        fat = fat,
        fiber = fiber,
        isIndian = isIndian
    )
}
