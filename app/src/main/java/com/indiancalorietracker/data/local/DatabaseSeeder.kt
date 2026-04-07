package com.indiancalorietracker.data.local

import com.indiancalorietracker.data.local.dao.FoodItemDao
import com.indiancalorietracker.data.local.dao.UserSettingsDao
import com.indiancalorietracker.data.local.entity.FoodItemEntity
import com.indiancalorietracker.data.local.entity.UserSettingsEntity
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Database seeder for initial data population.
 * This handles seeding food items and default user settings on first launch.
 */
@Singleton
class DatabaseSeeder @Inject constructor(
    private val foodItemDao: FoodItemDao,
    private val userSettingsDao: UserSettingsDao
) {
    /**
     * Seeds the database with initial data if empty.
     * Call this from Repository initialization or using a coroutine.
     */
    suspend fun seedIfEmpty() {
        // Check if already seeded
        val existingFoods = foodItemDao.getAllFoodItems().first()
        if (existingFoods.isNotEmpty()) {
            return // Already seeded
        }

        // Seed default user settings
        userSettingsDao.insertOrUpdate(UserSettingsEntity())

        // Seed Indian food data
        foodItemDao.insertAll(getIndianFoodData())
    }

    private fun getIndianFoodData(): List<FoodItemEntity> {
        return listOf(
            // Dal (Lentils)
            FoodItemEntity(name = "Dal Tadka", category = "Dal", caloriesPerServing = 180, servingSize = 150f, servingUnit = "g", protein = 8f, carbs = 25f, fat = 5f, fiber = 4f),
            FoodItemEntity(name = "Dal Makhani", category = "Dal", caloriesPerServing = 280, servingSize = 150f, servingUnit = "g", protein = 12f, carbs = 30f, fat = 12f, fiber = 6f),
            FoodItemEntity(name = "Chana Dal", category = "Dal", caloriesPerServing = 200, servingSize = 150f, servingUnit = "g", protein = 10f, carbs = 28f, fat = 6f, fiber = 8f),
            FoodItemEntity(name = "Masoor Dal", category = "Dal", caloriesPerServing = 160, servingSize = 150f, servingUnit = "g", protein = 9f, carbs = 22f, fat = 4f, fiber = 5f),
            FoodItemEntity(name = "Moong Dal", category = "Dal", caloriesPerServing = 150, servingSize = 150f, servingUnit = "g", protein = 10f, carbs = 20f, fat = 3f, fiber = 4f),
            FoodItemEntity(name = "Urad Dal", category = "Dal", caloriesPerServing = 170, servingSize = 150f, servingUnit = "g", protein = 11f, carbs = 22f, fat = 4f, fiber = 5f),
            FoodItemEntity(name = "Rajma", category = "Dal", caloriesPerServing = 220, servingSize = 150f, servingUnit = "g", protein = 12f, carbs = 30f, fat = 6f, fiber = 7f),
            FoodItemEntity(name = "Chole", category = "Dal", caloriesPerServing = 210, servingSize = 150f, servingUnit = "g", protein = 11f, carbs = 28f, fat = 6f, fiber = 8f),

            // Paneer
            FoodItemEntity(name = "Paneer Butter Masala", category = "Paneer", caloriesPerServing = 350, servingSize = 200f, servingUnit = "g", protein = 18f, carbs = 15f, fat = 25f, fiber = 2f),
            FoodItemEntity(name = "Palak Paneer", category = "Paneer", caloriesPerServing = 280, servingSize = 200f, servingUnit = "g", protein = 16f, carbs = 12f, fat = 20f, fiber = 4f),
            FoodItemEntity(name = "Paneer Tikka", category = "Paneer", caloriesPerServing = 290, servingSize = 150f, servingUnit = "g", protein = 20f, carbs = 10f, fat = 20f, fiber = 2f),
            FoodItemEntity(name = "Shahi Paneer", category = "Paneer", caloriesPerServing = 380, servingSize = 200f, servingUnit = "g", protein = 17f, carbs = 14f, fat = 30f, fiber = 2f),
            FoodItemEntity(name = "Matar Paneer", category = "Paneer", caloriesPerServing = 260, servingSize = 200f, servingUnit = "g", protein = 14f, carbs = 18f, fat = 16f, fiber = 4f),

            // Non-Veg
            FoodItemEntity(name = "Butter Chicken", category = "Non-Veg", caloriesPerServing = 420, servingSize = 200f, servingUnit = "g", protein = 25f, carbs = 12f, fat = 32f, fiber = 2f),
            FoodItemEntity(name = "Chicken Tikka Masala", category = "Non-Veg", caloriesPerServing = 380, servingSize = 200f, servingUnit = "g", protein = 28f, carbs = 15f, fat = 25f, fiber = 3f),
            FoodItemEntity(name = "Tandoori Chicken", category = "Non-Veg", caloriesPerServing = 260, servingSize = 150f, servingUnit = "g", protein = 35f, carbs = 5f, fat = 12f, fiber = 1f),
            FoodItemEntity(name = "Chicken Biryani", category = "Rice", caloriesPerServing = 350, servingSize = 250f, servingUnit = "g", protein = 20f, carbs = 40f, fat = 14f, fiber = 2f),
            FoodItemEntity(name = "Mutton Curry", category = "Non-Veg", caloriesPerServing = 380, servingSize = 200f, servingUnit = "g", protein = 24f, carbs = 10f, fat = 28f, fiber = 2f),
            FoodItemEntity(name = "Fish Curry", category = "Non-Veg", caloriesPerServing = 220, servingSize = 200f, servingUnit = "g", protein = 25f, carbs = 8f, fat = 12f, fiber = 2f),
            FoodItemEntity(name = "Egg Curry", category = "Non-Veg", caloriesPerServing = 200, servingSize = 200f, servingUnit = "g", protein = 14f, carbs = 10f, fat = 14f, fiber = 2f),

            // Rice
            FoodItemEntity(name = "Jeera Rice", category = "Rice", caloriesPerServing = 200, servingSize = 150f, servingUnit = "g", protein = 4f, carbs = 35f, fat = 5f, fiber = 1f),
            FoodItemEntity(name = "Vegetable Pulao", category = "Rice", caloriesPerServing = 220, servingSize = 150f, servingUnit = "g", protein = 5f, carbs = 38f, fat = 6f, fiber = 3f),
            FoodItemEntity(name = "Lemon Rice", category = "Rice", caloriesPerServing = 210, servingSize = 150f, servingUnit = "g", protein = 4f, carbs = 36f, fat = 6f, fiber = 2f),
            FoodItemEntity(name = "Curd Rice", category = "Rice", caloriesPerServing = 180, servingSize = 150f, servingUnit = "g", protein = 5f, carbs = 30f, fat = 4f, fiber = 1f),
            FoodItemEntity(name = "Khichdi", category = "Rice", caloriesPerServing = 200, servingSize = 200f, servingUnit = "g", protein = 8f, carbs = 32f, fat = 5f, fiber = 4f),

            // Breads
            FoodItemEntity(name = "Roti", category = "Breads", caloriesPerServing = 100, servingSize = 30f, servingUnit = "g", protein = 3f, carbs = 18f, fat = 2f, fiber = 2f),
            FoodItemEntity(name = "Naan", category = "Breads", caloriesPerServing = 180, servingSize = 60f, servingUnit = "g", protein = 5f, carbs = 28f, fat = 5f, fiber = 1f),
            FoodItemEntity(name = "Butter Naan", category = "Breads", caloriesPerServing = 220, servingSize = 60f, servingUnit = "g", protein = 5f, carbs = 30f, fat = 10f, fiber = 1f),
            FoodItemEntity(name = "Paratha", category = "Breads", caloriesPerServing = 180, servingSize = 60f, servingUnit = "g", protein = 4f, carbs = 25f, fat = 8f, fiber = 2f),
            FoodItemEntity(name = "Aloo Paratha", category = "Breads", caloriesPerServing = 220, servingSize = 80f, servingUnit = "g", protein = 5f, carbs = 32f, fat = 9f, fiber = 3f),
            FoodItemEntity(name = "Puri", category = "Breads", caloriesPerServing = 100, servingSize = 25f, servingUnit = "g", protein = 2f, carbs = 12f, fat = 5f, fiber = 1f),

            // Vegetables
            FoodItemEntity(name = "Aloo Gobi", category = "Vegetable", caloriesPerServing = 180, servingSize = 200f, servingUnit = "g", protein = 4f, carbs = 22f, fat = 10f, fiber = 4f),
            FoodItemEntity(name = "Baingan Bharta", category = "Vegetable", caloriesPerServing = 150, servingSize = 200f, servingUnit = "g", protein = 3f, carbs = 15f, fat = 9f, fiber = 5f),
            FoodItemEntity(name = "Aloo Matar", category = "Vegetable", caloriesPerServing = 170, servingSize = 200f, servingUnit = "g", protein = 5f, carbs = 24f, fat = 7f, fiber = 4f),
            FoodItemEntity(name = "Mixed Vegetable", category = "Vegetable", caloriesPerServing = 160, servingSize = 200f, servingUnit = "g", protein = 4f, carbs = 20f, fat = 8f, fiber = 5f),
            FoodItemEntity(name = "Bhindi Masala", category = "Vegetable", caloriesPerServing = 140, servingSize = 200f, servingUnit = "g", protein = 3f, carbs = 12f, fat = 9f, fiber = 4f),
            FoodItemEntity(name = "Palak Corn", category = "Vegetable", caloriesPerServing = 150, servingSize = 200f, servingUnit = "g", protein = 5f, carbs = 16f, fat = 7f, fiber = 4f),

            // South Indian
            FoodItemEntity(name = "Idli", category = "South Indian", caloriesPerServing = 60, servingSize = 30f, servingUnit = "g", protein = 2f, carbs = 12f, fat = 0.5f, fiber = 1f),
            FoodItemEntity(name = "Dosa (Plain)", category = "South Indian", caloriesPerServing = 120, servingSize = 80f, servingUnit = "g", protein = 3f, carbs = 20f, fat = 4f, fiber = 1f),
            FoodItemEntity(name = "Masala Dosa", category = "South Indian", caloriesPerServing = 180, servingSize = 100f, servingUnit = "g", protein = 4f, carbs = 25f, fat = 7f, fiber = 2f),
            FoodItemEntity(name = "Uttapam", category = "South Indian", caloriesPerServing = 150, servingSize = 100f, servingUnit = "g", protein = 4f, carbs = 22f, fat = 5f, fiber = 2f),
            FoodItemEntity(name = "Sambar", category = "South Indian", caloriesPerServing = 140, servingSize = 200f, servingUnit = "g", protein = 6f, carbs = 20f, fat = 4f, fiber = 5f),
            FoodItemEntity(name = "Rasam", category = "South Indian", caloriesPerServing = 60, servingSize = 200f, servingUnit = "g", protein = 2f, carbs = 10f, fat = 2f, fiber = 2f),
            FoodItemEntity(name = "Medu Vada", category = "South Indian", caloriesPerServing = 100, servingSize = 40f, servingUnit = "g", protein = 4f, carbs = 12f, fat = 4f, fiber = 2f),
            FoodItemEntity(name = "Upma", category = "South Indian", caloriesPerServing = 170, servingSize = 150f, servingUnit = "g", protein = 4f, carbs = 25f, fat = 7f, fiber = 3f),

            // Snacks
            FoodItemEntity(name = "Samosa", category = "Snacks", caloriesPerServing = 180, servingSize = 60f, servingUnit = "g", protein = 3f, carbs = 22f, fat = 10f, fiber = 2f),
            FoodItemEntity(name = "Pakora", category = "Snacks", caloriesPerServing = 120, servingSize = 50f, servingUnit = "g", protein = 3f, carbs = 12f, fat = 7f, fiber = 2f),
            FoodItemEntity(name = "Vada Pav", category = "Snacks", caloriesPerServing = 280, servingSize = 100f, servingUnit = "g", protein = 5f, carbs = 40f, fat = 12f, fiber = 3f),
            FoodItemEntity(name = "Pav Bhaji", category = "Snacks", caloriesPerServing = 350, servingSize = 250f, servingUnit = "g", protein = 8f, carbs = 50f, fat = 14f, fiber = 6f),
            FoodItemEntity(name = "Bhel Puri", category = "Snacks", caloriesPerServing = 180, servingSize = 80f, servingUnit = "g", protein = 4f, carbs = 30f, fat = 6f, fiber = 4f),
            FoodItemEntity(name = "Pani Puri", category = "Snacks", caloriesPerServing = 200, servingSize = 6f, servingUnit = "pcs", protein = 3f, carbs = 35f, fat = 6f, fiber = 2f),
            FoodItemEntity(name = "Dahi Puri", category = "Snacks", caloriesPerServing = 200, servingSize = 100f, servingUnit = "g", protein = 5f, carbs = 28f, fat = 8f, fiber = 2f),

            // Beverages
            FoodItemEntity(name = "Masala Chai", category = "Beverages", caloriesPerServing = 100, servingSize = 200f, servingUnit = "ml", protein = 3f, carbs = 12f, fat = 4f, fiber = 0f),
            FoodItemEntity(name = "Masala Chai (Sweet)", category = "Beverages", caloriesPerServing = 140, servingSize = 200f, servingUnit = "ml", protein = 3f, carbs = 20f, fat = 5f, fiber = 0f),
            FoodItemEntity(name = "Filter Coffee", category = "Beverages", caloriesPerServing = 120, servingSize = 200f, servingUnit = "ml", protein = 3f, carbs = 10f, fat = 7f, fiber = 0f),
            FoodItemEntity(name = "Sweet Lassi", category = "Beverages", caloriesPerServing = 180, servingSize = 250f, servingUnit = "ml", protein = 6f, carbs = 28f, fat = 5f, fiber = 0f),
            FoodItemEntity(name = "Buttermilk", category = "Beverages", caloriesPerServing = 40, servingSize = 250f, servingUnit = "ml", protein = 2f, carbs = 5f, fat = 1f, fiber = 0f),

            // Desserts
            FoodItemEntity(name = "Gulab Jamun", category = "Desserts", caloriesPerServing = 150, servingSize = 40f, servingUnit = "g", protein = 2f, carbs = 22f, fat = 6f, fiber = 0f),
            FoodItemEntity(name = "Kheer (Rice)", category = "Desserts", caloriesPerServing = 200, servingSize = 150f, servingUnit = "g", protein = 5f, carbs = 30f, fat = 7f, fiber = 0f),
            FoodItemEntity(name = "Rasmalai", category = "Desserts", caloriesPerServing = 180, servingSize = 100f, servingUnit = "g", protein = 5f, carbs = 24f, fat = 7f, fiber = 0f),
            FoodItemEntity(name = "Rasgulla", category = "Desserts", caloriesPerServing = 120, servingSize = 80f, servingUnit = "g", protein = 4f, carbs = 20f, fat = 3f, fiber = 0f),
            FoodItemEntity(name = "Jalebi", category = "Desserts", caloriesPerServing = 150, servingSize = 50f, servingUnit = "g", protein = 2f, carbs = 28f, fat = 4f, fiber = 0f),
            FoodItemEntity(name = "Ladoo (Besan)", category = "Desserts", caloriesPerServing = 180, servingSize = 40f, servingUnit = "g", protein = 3f, carbs = 22f, fat = 9f, fiber = 1f),
            FoodItemEntity(name = "Halwa (Gajar)", category = "Desserts", caloriesPerServing = 220, servingSize = 100f, servingUnit = "g", protein = 3f, carbs = 30f, fat = 11f, fiber = 2f),
            FoodItemEntity(name = "Kulfi", category = "Desserts", caloriesPerServing = 160, servingSize = 80f, servingUnit = "g", protein = 4f, carbs = 18f, fat = 8f, fiber = 0f),

            // Side Dishes
            FoodItemEntity(name = "Raita (Boondi)", category = "Side Dish", caloriesPerServing = 120, servingSize = 100f, servingUnit = "g", protein = 4f, carbs = 14f, fat = 5f, fiber = 1f),
            FoodItemEntity(name = "Raita (Cucumber)", category = "Side Dish", caloriesPerServing = 80, servingSize = 100f, servingUnit = "g", protein = 3f, carbs = 8f, fat = 4f, fiber = 1f),
            FoodItemEntity(name = "Green Chutney", category = "Side Dish", caloriesPerServing = 30, servingSize = 30f, servingUnit = "g", protein = 1f, carbs = 4f, fat = 1f, fiber = 2f),
            FoodItemEntity(name = "Tamarind Chutney", category = "Side Dish", caloriesPerServing = 60, servingSize = 30f, servingUnit = "g", protein = 0f, carbs = 15f, fat = 0f, fiber = 1f)
        )
    }
}