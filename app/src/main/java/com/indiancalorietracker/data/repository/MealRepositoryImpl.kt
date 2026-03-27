package com.indiancalorietracker.data.repository

import com.indiancalorietracker.data.local.dao.FoodItemDao
import com.indiancalorietracker.data.local.dao.MealLogDao
import com.indiancalorietracker.data.local.entity.MealLogEntity
import com.indiancalorietracker.domain.model.FoodItem
import com.indiancalorietracker.domain.model.MealLog
import com.indiancalorietracker.domain.model.MealType
import com.indiancalorietracker.domain.repository.MealRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealRepositoryImpl @Inject constructor(
    private val mealLogDao: MealLogDao,
    private val foodItemDao: FoodItemDao
) : MealRepository {

    override fun getMealLogsByDate(date: Long): Flow<List<MealLog>> {
        return mealLogDao.getMealLogsByDate(date).map { entities ->
            entities.mapNotNull { entity ->
                val foodItem = foodItemDao.getFoodItemById(entity.foodItemId)
                foodItem?.let {
                    MealLog(
                        id = entity.id,
                        foodItem = FoodItem(
                            id = it.id,
                            name = it.name,
                            nameHindi = it.nameHindi,
                            category = it.category,
                            caloriesPerServing = it.caloriesPerServing,
                            servingSize = it.servingSize,
                            servingUnit = it.servingUnit,
                            protein = it.protein,
                            carbs = it.carbs,
                            fat = it.fat,
                            fiber = it.fiber,
                            isIndian = it.isIndian
                        ),
                        date = entity.date,
                        mealType = MealType.valueOf(entity.mealType),
                        servings = entity.servings,
                        totalCalories = entity.totalCalories,
                        timestamp = entity.timestamp
                    )
                }
            }
        }
    }

    override fun getMealLogsBetweenDates(startDate: Long, endDate: Long): Flow<List<MealLog>> {
        return mealLogDao.getMealLogsBetweenDates(startDate, endDate).map { entities ->
            entities.mapNotNull { entity ->
                val foodItem = foodItemDao.getFoodItemById(entity.foodItemId)
                foodItem?.let {
                    MealLog(
                        id = entity.id,
                        foodItem = FoodItem(
                            id = it.id,
                            name = it.name,
                            nameHindi = it.nameHindi,
                            category = it.category,
                            caloriesPerServing = it.caloriesPerServing,
                            servingSize = it.servingSize,
                            servingUnit = it.servingUnit,
                            protein = it.protein,
                            carbs = it.carbs,
                            fat = it.fat,
                            fiber = it.fiber,
                            isIndian = it.isIndian
                        ),
                        date = entity.date,
                        mealType = MealType.valueOf(entity.mealType),
                        servings = entity.servings,
                        totalCalories = entity.totalCalories,
                        timestamp = entity.timestamp
                    )
                }
            }
        }
    }

    override fun getTotalCaloriesForDate(date: Long): Flow<Int> {
        return mealLogDao.getTotalCaloriesForDate(date).map { it ?: 0 }
    }

    override suspend fun logMeal(mealLog: MealLog): Long {
        val entity = MealLogEntity(
            id = if (mealLog.id == 0L) 0 else mealLog.id,
            foodItemId = mealLog.foodItem.id,
            date = mealLog.date,
            mealType = mealLog.mealType.name,
            servings = mealLog.servings,
            totalCalories = mealLog.totalCalories,
            timestamp = mealLog.timestamp
        )
        return mealLogDao.insert(entity)
    }

    override suspend fun deleteMealLog(id: Long) {
        mealLogDao.deleteById(id)
    }
}
