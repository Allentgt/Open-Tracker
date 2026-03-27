package com.indiancalorietracker.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.indiancalorietracker.data.local.dao.ExerciseDao
import com.indiancalorietracker.data.local.dao.FoodItemDao
import com.indiancalorietracker.data.local.dao.MealLogDao
import com.indiancalorietracker.data.local.dao.UserSettingsDao
import com.indiancalorietracker.data.local.dao.WaterIntakeDao
import com.indiancalorietracker.data.local.dao.WorkoutDao
import com.indiancalorietracker.data.local.entity.ExerciseEntity
import com.indiancalorietracker.data.local.entity.FoodItemEntity
import com.indiancalorietracker.data.local.entity.MealLogEntity
import com.indiancalorietracker.data.local.entity.UserSettingsEntity
import com.indiancalorietracker.data.local.entity.WaterIntakeEntity
import com.indiancalorietracker.data.local.entity.WorkoutExerciseEntity
import com.indiancalorietracker.data.local.entity.WorkoutSessionEntity
import com.indiancalorietracker.data.local.entity.WorkoutSetEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        FoodItemEntity::class, 
        MealLogEntity::class, 
        UserSettingsEntity::class,
        WorkoutSessionEntity::class,
        ExerciseEntity::class,
        WorkoutExerciseEntity::class,
        WorkoutSetEntity::class,
        WaterIntakeEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class CalorieDatabase : RoomDatabase() {
    abstract fun foodItemDao(): FoodItemDao
    abstract fun mealLogDao(): MealLogDao
    abstract fun userSettingsDao(): UserSettingsDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun waterIntakeDao(): WaterIntakeDao

    companion object {
        @Volatile
        private var INSTANCE: CalorieDatabase? = null

        fun getDatabase(context: Context): CalorieDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CalorieDatabase::class.java,
                    "calorie_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database.foodItemDao(), database.userSettingsDao())
                }
            }
        }

        private suspend fun populateDatabase(
            foodItemDao: FoodItemDao,
            userSettingsDao: UserSettingsDao
        ) {
            userSettingsDao.insertOrUpdate(UserSettingsEntity())
            foodItemDao.insertAll(getIndianFoodData())
        }

        private fun getIndianFoodData(): List<FoodItemEntity> {
            return listOf(
                FoodItemEntity(name = "Dal Tadka", category = "Dal", caloriesPerServing = 180, servingSize = 150f, servingUnit = "g", protein = 8f, carbs = 25f, fat = 5f, fiber = 4f),
                FoodItemEntity(name = "Dal Makhani", category = "Dal", caloriesPerServing = 280, servingSize = 150f, servingUnit = "g", protein = 12f, carbs = 30f, fat = 12f, fiber = 6f),
                FoodItemEntity(name = "Chana Dal", category = "Dal", caloriesPerServing = 200, servingSize = 150f, servingUnit = "g", protein = 10f, carbs = 28f, fat = 6f, fiber = 8f),
                FoodItemEntity(name = "Masoor Dal", category = "Dal", caloriesPerServing = 160, servingSize = 150f, servingUnit = "g", protein = 9f, carbs = 22f, fat = 4f, fiber = 5f),
                FoodItemEntity(name = "Moong Dal", category = "Dal", caloriesPerServing = 150, servingSize = 150f, servingUnit = "g", protein = 10f, carbs = 20f, fat = 3f, fiber = 4f),
                FoodItemEntity(name = "Urad Dal", category = "Dal", caloriesPerServing = 170, servingSize = 150f, servingUnit = "g", protein = 11f, carbs = 22f, fat = 4f, fiber = 5f),
                FoodItemEntity(name = "Rajma", category = "Dal", caloriesPerServing = 220, servingSize = 150f, servingUnit = "g", protein = 12f, carbs = 30f, fat = 6f, fiber = 7f),
                FoodItemEntity(name = "Chole", category = "Dal", caloriesPerServing = 210, servingSize = 150f, servingUnit = "g", protein = 11f, carbs = 28f, fat = 6f, fiber = 8f),

                FoodItemEntity(name = "Paneer Butter Masala", category = "Paneer", caloriesPerServing = 350, servingSize = 200f, servingUnit = "g", protein = 18f, carbs = 15f, fat = 25f, fiber = 2f),
                FoodItemEntity(name = "Palak Paneer", category = "Paneer", caloriesPerServing = 280, servingSize = 200f, servingUnit = "g", protein = 16f, carbs = 12f, fat = 20f, fiber = 4f),
                FoodItemEntity(name = "Paneer Tikka", category = "Paneer", caloriesPerServing = 290, servingSize = 150f, servingUnit = "g", protein = 20f, carbs = 10f, fat = 20f, fiber = 2f),
                FoodItemEntity(name = "Shahi Paneer", category = "Paneer", caloriesPerServing = 380, servingSize = 200f, servingUnit = "g", protein = 17f, carbs = 14f, fat = 30f, fiber = 2f),
                FoodItemEntity(name = "Matar Paneer", category = "Paneer", caloriesPerServing = 260, servingSize = 200f, servingUnit = "g", protein = 14f, carbs = 18f, fat = 16f, fiber = 4f),
                FoodItemEntity(name = "Kadai Paneer", category = "Paneer", caloriesPerServing = 300, servingSize = 200f, servingUnit = "g", protein = 16f, carbs = 14f, fat = 22f, fiber = 3f),
                FoodItemEntity(name = "Paneer Lababdar", category = "Paneer", caloriesPerServing = 320, servingSize = 200f, servingUnit = "g", protein = 17f, carbs = 16f, fat = 24f, fiber = 2f),

                FoodItemEntity(name = "Butter Chicken", category = "Non-Veg", caloriesPerServing = 420, servingSize = 200f, servingUnit = "g", protein = 25f, carbs = 12f, fat = 32f, fiber = 2f),
                FoodItemEntity(name = "Chicken Tikka Masala", category = "Non-Veg", caloriesPerServing = 380, servingSize = 200f, servingUnit = "g", protein = 28f, carbs = 15f, fat = 25f, fiber = 3f),
                FoodItemEntity(name = "Tandoori Chicken", category = "Non-Veg", caloriesPerServing = 260, servingSize = 150f, servingUnit = "g", protein = 35f, carbs = 5f, fat = 12f, fiber = 1f),
                FoodItemEntity(name = "Chicken Biryani", category = "Rice", caloriesPerServing = 350, servingSize = 250f, servingUnit = "g", protein = 20f, carbs = 40f, fat = 14f, fiber = 2f),
                FoodItemEntity(name = "Mutton Curry", category = "Non-Veg", caloriesPerServing = 380, servingSize = 200f, servingUnit = "g", protein = 24f, carbs = 10f, fat = 28f, fiber = 2f),
                FoodItemEntity(name = "Fish Curry", category = "Non-Veg", caloriesPerServing = 220, servingSize = 200f, servingUnit = "g", protein = 25f, carbs = 8f, fat = 12f, fiber = 2f),
                FoodItemEntity(name = "Prawn Curry", category = "Non-Veg", caloriesPerServing = 240, servingSize = 200f, servingUnit = "g", protein = 28f, carbs = 6f, fat = 12f, fiber = 1f),
                FoodItemEntity(name = "Egg Curry", category = "Non-Veg", caloriesPerServing = 200, servingSize = 200f, servingUnit = "g", protein = 14f, carbs = 10f, fat = 14f, fiber = 2f),
                FoodItemEntity(name = "Keema", category = "Non-Veg", caloriesPerServing = 300, servingSize = 150f, servingUnit = "g", protein = 22f, carbs = 8f, fat = 22f, fiber = 1f),

                FoodItemEntity(name = "Jeera Rice", category = "Rice", caloriesPerServing = 200, servingSize = 150f, servingUnit = "g", protein = 4f, carbs = 35f, fat = 5f, fiber = 1f),
                FoodItemEntity(name = "Vegetable Pulao", category = "Rice", caloriesPerServing = 220, servingSize = 150f, servingUnit = "g", protein = 5f, carbs = 38f, fat = 6f, fiber = 3f),
                FoodItemEntity(name = "Lemon Rice", category = "Rice", caloriesPerServing = 210, servingSize = 150f, servingUnit = "g", protein = 4f, carbs = 36f, fat = 6f, fiber = 2f),
                FoodItemEntity(name = "Curd Rice", category = "Rice", caloriesPerServing = 180, servingSize = 150f, servingUnit = "g", protein = 5f, carbs = 30f, fat = 4f, fiber = 1f),
                FoodItemEntity(name = "Khichdi", category = "Rice", caloriesPerServing = 200, servingSize = 200f, servingUnit = "g", protein = 8f, carbs = 32f, fat = 5f, fiber = 4f),
                FoodItemEntity(name = "Vegetable Biryani", category = "Rice", caloriesPerServing = 280, servingSize = 200f, servingUnit = "g", protein = 6f, carbs = 42f, fat = 10f, fiber = 4f),
                FoodItemEntity(name = "Paneer Biryani", category = "Rice", caloriesPerServing = 340, servingSize = 200f, servingUnit = "g", protein = 14f, carbs = 40f, fat = 14f, fiber = 3f),

                FoodItemEntity(name = "Roti", category = "Breads", caloriesPerServing = 100, servingSize = 30f, servingUnit = "g", protein = 3f, carbs = 18f, fat = 2f, fiber = 2f),
                FoodItemEntity(name = "Naan", category = "Breads", caloriesPerServing = 180, servingSize = 60f, servingUnit = "g", protein = 5f, carbs = 28f, fat = 5f, fiber = 1f),
                FoodItemEntity(name = "Butter Naan", category = "Breads", caloriesPerServing = 220, servingSize = 60f, servingUnit = "g", protein = 5f, carbs = 30f, fat = 10f, fiber = 1f),
                FoodItemEntity(name = "Garlic Naan", category = "Breads", caloriesPerServing = 200, servingSize = 60f, servingUnit = "g", protein = 5f, carbs = 29f, fat = 7f, fiber = 1f),
                FoodItemEntity(name = "Paratha", category = "Breads", caloriesPerServing = 180, servingSize = 60f, servingUnit = "g", protein = 4f, carbs = 25f, fat = 8f, fiber = 2f),
                FoodItemEntity(name = "Aloo Paratha", category = "Breads", caloriesPerServing = 220, servingSize = 80f, servingUnit = "g", protein = 5f, carbs = 32f, fat = 9f, fiber = 3f),
                FoodItemEntity(name = "Gobi Paratha", category = "Breads", caloriesPerServing = 210, servingSize = 80f, servingUnit = "g", protein = 5f, carbs = 30f, fat = 9f, fiber = 3f),
                FoodItemEntity(name = "Missi Roti", category = "Breads", caloriesPerServing = 120, servingSize = 40f, servingUnit = "g", protein = 5f, carbs = 18f, fat = 3f, fiber = 4f),
                FoodItemEntity(name = "Puri", category = "Breads", caloriesPerServing = 100, servingSize = 25f, servingUnit = "g", protein = 2f, carbs = 12f, fat = 5f, fiber = 1f),
                FoodItemEntity(name = "Bhatura", category = "Breads", caloriesPerServing = 180, servingSize = 50f, servingUnit = "g", protein = 4f, carbs = 22f, fat = 9f, fiber = 1f),
                FoodItemEntity(name = "Kulcha", category = "Breads", caloriesPerServing = 190, servingSize = 60f, servingUnit = "g", protein = 4f, carbs = 28f, fat = 7f, fiber = 1f),

                FoodItemEntity(name = "Aloo Gobi", category = "Vegetable", caloriesPerServing = 180, servingSize = 200f, servingUnit = "g", protein = 4f, carbs = 22f, fat = 10f, fiber = 4f),
                FoodItemEntity(name = "Baingan Bharta", category = "Vegetable", caloriesPerServing = 150, servingSize = 200f, servingUnit = "g", protein = 3f, carbs = 15f, fat = 9f, fiber = 5f),
                FoodItemEntity(name = "Aloo Matar", category = "Vegetable", caloriesPerServing = 170, servingSize = 200f, servingUnit = "g", protein = 5f, carbs = 24f, fat = 7f, fiber = 4f),
                FoodItemEntity(name = "Mixed Vegetable", category = "Vegetable", caloriesPerServing = 160, servingSize = 200f, servingUnit = "g", protein = 4f, carbs = 20f, fat = 8f, fiber = 5f),
                FoodItemEntity(name = "Bhindi Masala", category = "Vegetable", caloriesPerServing = 140, servingSize = 200f, servingUnit = "g", protein = 3f, carbs = 12f, fat = 9f, fiber = 4f),
                FoodItemEntity(name = "Lauki ki Sabzi", category = "Vegetable", caloriesPerServing = 120, servingSize = 200f, servingUnit = "g", protein = 3f, carbs = 14f, fat = 6f, fiber = 3f),
                FoodItemEntity(name = "Tinda Masala", category = "Vegetable", caloriesPerServing = 130, servingSize = 200f, servingUnit = "g", protein = 3f, carbs = 15f, fat = 7f, fiber = 3f),
                FoodItemEntity(name = "Karela Fry", category = "Vegetable", caloriesPerServing = 140, servingSize = 150f, servingUnit = "g", protein = 2f, carbs = 10f, fat = 10f, fiber = 4f),
                FoodItemEntity(name = "Methi Aloo", category = "Vegetable", caloriesPerServing = 160, servingSize = 200f, servingUnit = "g", protein = 4f, carbs = 20f, fat = 8f, fiber = 4f),
                FoodItemEntity(name = "Palak Corn", category = "Vegetable", caloriesPerServing = 150, servingSize = 200f, servingUnit = "g", protein = 5f, carbs = 16f, fat = 7f, fiber = 4f),
                FoodItemEntity(name = "Chole Masala", category = "Vegetable", caloriesPerServing = 210, servingSize = 200f, servingUnit = "g", protein = 11f, carbs = 28f, fat = 7f, fiber = 8f),
                FoodItemEntity(name = "Jeera Aloo", category = "Vegetable", caloriesPerServing = 170, servingSize = 200f, servingUnit = "g", protein = 4f, carbs = 24f, fat = 7f, fiber = 3f),

                FoodItemEntity(name = "Idli", category = "South Indian", caloriesPerServing = 60, servingSize = 30f, servingUnit = "g", protein = 2f, carbs = 12f, fat = 0.5f, fiber = 1f),
                FoodItemEntity(name = "Dosa (Plain)", category = "South Indian", caloriesPerServing = 120, servingSize = 80f, servingUnit = "g", protein = 3f, carbs = 20f, fat = 4f, fiber = 1f),
                FoodItemEntity(name = "Masala Dosa", category = "South Indian", caloriesPerServing = 180, servingSize = 100f, servingUnit = "g", protein = 4f, carbs = 25f, fat = 7f, fiber = 2f),
                FoodItemEntity(name = "Uttapam", category = "South Indian", caloriesPerServing = 150, servingSize = 100f, servingUnit = "g", protein = 4f, carbs = 22f, fat = 5f, fiber = 2f),
                FoodItemEntity(name = "Sambar", category = "South Indian", caloriesPerServing = 140, servingSize = 200f, servingUnit = "g", protein = 6f, carbs = 20f, fat = 4f, fiber = 5f),
                FoodItemEntity(name = "Rasam", category = "South Indian", caloriesPerServing = 60, servingSize = 200f, servingUnit = "g", protein = 2f, carbs = 10f, fat = 2f, fiber = 2f),
                FoodItemEntity(name = "Coconut Chutney", category = "South Indian", caloriesPerServing = 80, servingSize = 50f, servingUnit = "g", protein = 1f, carbs = 6f, fat = 6f, fiber = 2f),
                FoodItemEntity(name = "Medu Vada", category = "South Indian", caloriesPerServing = 100, servingSize = 40f, servingUnit = "g", protein = 4f, carbs = 12f, fat = 4f, fiber = 2f),
                FoodItemEntity(name = "Pongal", category = "South Indian", caloriesPerServing = 200, servingSize = 150f, servingUnit = "g", protein = 5f, carbs = 30f, fat = 7f, fiber = 2f),
                FoodItemEntity(name = "Upma", category = "South Indian", caloriesPerServing = 170, servingSize = 150f, servingUnit = "g", protein = 4f, carbs = 25f, fat = 7f, fiber = 3f),
                FoodItemEntity(name = "Set Dosa", category = "South Indian", caloriesPerServing = 180, servingSize = 100f, servingUnit = "g", protein = 4f, carbs = 35f, fat = 2f, fiber = 2f),
                FoodItemEntity(name = "Pesarattu", category = "South Indian", caloriesPerServing = 160, servingSize = 100f, servingUnit = "g", protein = 8f, carbs = 24f, fat = 4f, fiber = 4f),

                FoodItemEntity(name = "Samosa", category = "Snacks", caloriesPerServing = 180, servingSize = 60f, servingUnit = "g", protein = 3f, carbs = 22f, fat = 10f, fiber = 2f),
                FoodItemEntity(name = "Samosa (2 pcs)", category = "Snacks", caloriesPerServing = 320, servingSize = 120f, servingUnit = "g", protein = 5f, carbs = 38f, fat = 17f, fiber = 3f),
                FoodItemEntity(name = "Pakora", category = "Snacks", caloriesPerServing = 120, servingSize = 50f, servingUnit = "g", protein = 3f, carbs = 12f, fat = 7f, fiber = 2f),
                FoodItemEntity(name = "Aloo Tikki", category = "Snacks", caloriesPerServing = 150, servingSize = 80f, servingUnit = "g", protein = 2f, carbs = 20f, fat = 7f, fiber = 2f),
                FoodItemEntity(name = "Vada Pav", category = "Snacks", caloriesPerServing = 280, servingSize = 100f, servingUnit = "g", protein = 5f, carbs = 40f, fat = 12f, fiber = 3f),
                FoodItemEntity(name = "Pav Bhaji", category = "Snacks", caloriesPerServing = 350, servingSize = 250f, servingUnit = "g", protein = 8f, carbs = 50f, fat = 14f, fiber = 6f),
                FoodItemEntity(name = "Dahi Puri", category = "Snacks", caloriesPerServing = 200, servingSize = 100f, servingUnit = "g", protein = 5f, carbs = 28f, fat = 8f, fiber = 2f),
                FoodItemEntity(name = "Bhel Puri", category = "Snacks", caloriesPerServing = 180, servingSize = 80f, servingUnit = "g", protein = 4f, carbs = 30f, fat = 6f, fiber = 4f),
                FoodItemEntity(name = "Sev Puri", category = "Snacks", caloriesPerServing = 220, servingSize = 100f, servingUnit = "g", protein = 4f, carbs = 32f, fat = 9f, fiber = 3f),
                FoodItemEntity(name = "Pani Puri", category = "Snacks", caloriesPerServing = 200, servingSize = 6f, servingUnit = "pcs", protein = 3f, carbs = 35f, fat = 6f, fiber = 2f),
                FoodItemEntity(name = "Dabeli", category = "Snacks", caloriesPerServing = 260, servingSize = 100f, servingUnit = "g", protein = 4f, carbs = 38f, fat = 11f, fiber = 3f),
                FoodItemEntity(name = "Kachori", category = "Snacks", caloriesPerServing = 190, servingSize = 50f, servingUnit = "g", protein = 3f, carbs = 22f, fat = 10f, fiber = 2f),
                FoodItemEntity(name = "Aloo Chaat", category = "Snacks", caloriesPerServing = 200, servingSize = 100f, servingUnit = "g", protein = 3f, carbs = 30f, fat = 8f, fiber = 3f),
                FoodItemEntity(name = "Papdi Chaat", category = "Snacks", caloriesPerServing = 250, servingSize = 100f, servingUnit = "g", protein = 5f, carbs = 35f, fat = 11f, fiber = 3f),

                FoodItemEntity(name = "Masala Chai", category = "Beverages", caloriesPerServing = 100, servingSize = 200f, servingUnit = "ml", protein = 3f, carbs = 12f, fat = 4f, fiber = 0f),
                FoodItemEntity(name = "Masala Chai (Sweet)", category = "Beverages", caloriesPerServing = 140, servingSize = 200f, servingUnit = "ml", protein = 3f, carbs = 20f, fat = 5f, fiber = 0f),
                FoodItemEntity(name = "Filter Coffee", category = "Beverages", caloriesPerServing = 120, servingSize = 200f, servingUnit = "ml", protein = 3f, carbs = 10f, fat = 7f, fiber = 0f),
                FoodItemEntity(name = "Sweet Lassi", category = "Beverages", caloriesPerServing = 180, servingSize = 250f, servingUnit = "ml", protein = 6f, carbs = 28f, fat = 5f, fiber = 0f),
                FoodItemEntity(name = "Salt Lassi", category = "Beverages", caloriesPerServing = 120, servingSize = 250f, servingUnit = "ml", protein = 6f, carbs = 10f, fat = 5f, fiber = 0f),
                FoodItemEntity(name = "Mango Lassi", category = "Beverages", caloriesPerServing = 220, servingSize = 250f, servingUnit = "ml", protein = 5f, carbs = 38f, fat = 6f, fiber = 1f),
                FoodItemEntity(name = "Buttermilk", category = "Beverages", caloriesPerServing = 40, servingSize = 250f, servingUnit = "ml", protein = 2f, carbs = 5f, fat = 1f, fiber = 0f),
                FoodItemEntity(name = "Nimbu Pani", category = "Beverages", caloriesPerServing = 50, servingSize = 250f, servingUnit = "ml", protein = 0f, carbs = 12f, fat = 0f, fiber = 0f),
                FoodItemEntity(name = "Aam Panna", category = "Beverages", caloriesPerServing = 80, servingSize = 250f, servingUnit = "ml", protein = 0f, carbs = 20f, fat = 0f, fiber = 1f),
                FoodItemEntity(name = "Jaljeera", category = "Beverages", caloriesPerServing = 30, servingSize = 250f, servingUnit = "ml", protein = 0f, carbs = 7f, fat = 0f, fiber = 1f),
                FoodItemEntity(name = "Badam Milk", category = "Beverages", caloriesPerServing = 200, servingSize = 250f, servingUnit = "ml", protein = 7f, carbs = 20f, fat = 11f, fiber = 1f),
                FoodItemEntity(name = "Thandai", category = "Beverages", caloriesPerServing = 250, servingSize = 250f, servingUnit = "ml", protein = 8f, carbs = 30f, fat = 12f, fiber = 1f),

                FoodItemEntity(name = "Gulab Jamun", category = "Desserts", caloriesPerServing = 150, servingSize = 40f, servingUnit = "g", protein = 2f, carbs = 22f, fat = 6f, fiber = 0f),
                FoodItemEntity(name = "Gulab Jamun (2 pcs)", category = "Desserts", caloriesPerServing = 280, servingSize = 80f, servingUnit = "g", protein = 3f, carbs = 40f, fat = 12f, fiber = 0f),
                FoodItemEntity(name = "Kheer (Rice)", category = "Desserts", caloriesPerServing = 200, servingSize = 150f, servingUnit = "g", protein = 5f, carbs = 30f, fat = 7f, fiber = 0f),
                FoodItemEntity(name = "Badam Kheer", category = "Desserts", caloriesPerServing = 240, servingSize = 150f, servingUnit = "g", protein = 7f, carbs = 28f, fat = 12f, fiber = 1f),
                FoodItemEntity(name = "Rasmalai", category = "Desserts", caloriesPerServing = 180, servingSize = 100f, servingUnit = "g", protein = 5f, carbs = 24f, fat = 7f, fiber = 0f),
                FoodItemEntity(name = "Rasgulla", category = "Desserts", caloriesPerServing = 120, servingSize = 80f, servingUnit = "g", protein = 4f, carbs = 20f, fat = 3f, fiber = 0f),
                FoodItemEntity(name = "Jalebi", category = "Desserts", caloriesPerServing = 150, servingSize = 50f, servingUnit = "g", protein = 2f, carbs = 28f, fat = 4f, fiber = 0f),
                FoodItemEntity(name = "Jalebi (2 pcs)", category = "Desserts", caloriesPerServing = 280, servingSize = 100f, servingUnit = "g", protein = 3f, carbs = 52f, fat = 7f, fiber = 0f),
                FoodItemEntity(name = "Gujia", category = "Desserts", caloriesPerServing = 180, servingSize = 60f, servingUnit = "g", protein = 3f, carbs = 24f, fat = 8f, fiber = 1f),
                FoodItemEntity(name = "Barfi", category = "Desserts", caloriesPerServing = 150, servingSize = 30f, servingUnit = "g", protein = 2f, carbs = 18f, fat = 8f, fiber = 0f),
                FoodItemEntity(name = "Peda", category = "Desserts", caloriesPerServing = 120, servingSize = 30f, servingUnit = "g", protein = 3f, carbs = 15f, fat = 6f, fiber = 0f),
                FoodItemEntity(name = "Halwa (Gajar)", category = "Desserts", caloriesPerServing = 220, servingSize = 100f, servingUnit = "g", protein = 3f, carbs = 30f, fat = 11f, fiber = 2f),
                FoodItemEntity(name = "Halwa (Suji)", category = "Desserts", caloriesPerServing = 200, servingSize = 100f, servingUnit = "g", protein = 3f, carbs = 28f, fat = 9f, fiber = 1f),
                FoodItemEntity(name = "Halwa (Moong Dal)", category = "Desserts", caloriesPerServing = 230, servingSize = 100f, servingUnit = "g", protein = 6f, carbs = 30f, fat = 11f, fiber = 2f),
                FoodItemEntity(name = "Ladoo (Besan)", category = "Desserts", caloriesPerServing = 180, servingSize = 40f, servingUnit = "g", protein = 3f, carbs = 22f, fat = 9f, fiber = 1f),
                FoodItemEntity(name = "Ladoo (Motichoor)", category = "Desserts", caloriesPerServing = 160, servingSize = 40f, servingUnit = "g", protein = 2f, carbs = 24f, fat = 7f, fiber = 0f),
                FoodItemEntity(name = "Shrikhand", category = "Desserts", caloriesPerServing = 220, servingSize = 100f, servingUnit = "g", protein = 6f, carbs = 30f, fat = 9f, fiber = 0f),
                FoodItemEntity(name = "Kulfi", category = "Desserts", caloriesPerServing = 160, servingSize = 80f, servingUnit = "g", protein = 4f, carbs = 18f, fat = 8f, fiber = 0f),
                FoodItemEntity(name = "Falooda", category = "Desserts", caloriesPerServing = 300, servingSize = 300f, servingUnit = "ml", protein = 5f, carbs = 50f, fat = 10f, fiber = 1f),

                FoodItemEntity(name = "Raita (Boondi)", category = "Side Dish", caloriesPerServing = 120, servingSize = 100f, servingUnit = "g", protein = 4f, carbs = 14f, fat = 5f, fiber = 1f),
                FoodItemEntity(name = "Raita (Cucumber)", category = "Side Dish", caloriesPerServing = 80, servingSize = 100f, servingUnit = "g", protein = 3f, carbs = 8f, fat = 4f, fiber = 1f),
                FoodItemEntity(name = "Raita (Mixed Veg)", category = "Side Dish", caloriesPerServing = 90, servingSize = 100f, servingUnit = "g", protein = 3f, carbs = 10f, fat = 4f, fiber = 2f),
                FoodItemEntity(name = "Green Chutney", category = "Side Dish", caloriesPerServing = 30, servingSize = 30f, servingUnit = "g", protein = 1f, carbs = 4f, fat = 1f, fiber = 2f),
                FoodItemEntity(name = "Mint Chutney", category = "Side Dish", caloriesPerServing = 25, servingSize = 30f, servingUnit = "g", protein = 1f, carbs = 3f, fat = 1f, fiber = 1f),
                FoodItemEntity(name = "Tamarind Chutney", category = "Side Dish", caloriesPerServing = 60, servingSize = 30f, servingUnit = "g", protein = 0f, carbs = 15f, fat = 0f, fiber = 1f),
                FoodItemEntity(name = "Onion Salad", category = "Side Dish", caloriesPerServing = 30, servingSize = 50f, servingUnit = "g", protein = 1f, carbs = 5f, fat = 1f, fiber = 1f),
                FoodItemEntity(name = "Papad", category = "Side Dish", caloriesPerServing = 80, servingSize = 20f, servingUnit = "g", protein = 2f, carbs = 14f, fat = 2f, fiber = 1f),
                FoodItemEntity(name = "Achar (Mango)", category = "Side Dish", caloriesPerServing = 40, servingSize = 30f, servingUnit = "g", protein = 0f, carbs = 6f, fat = 2f, fiber = 1f),
                FoodItemEntity(name = "Koshambri", category = "Side Dish", caloriesPerServing = 80, servingSize = 80f, servingUnit = "g", protein = 3f, carbs = 12f, fat = 2f, fiber = 3f)
            )
        }
    }
}
