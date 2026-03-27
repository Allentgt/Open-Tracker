package com.indiancalorietracker.data.repository

import com.indiancalorietracker.data.local.dao.WaterIntakeDao
import com.indiancalorietracker.data.local.entity.WaterIntakeEntity
import com.indiancalorietracker.domain.repository.WaterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WaterRepositoryImpl @Inject constructor(
    private val waterIntakeDao: WaterIntakeDao
) : WaterRepository {

    override fun getWaterIntakeForDate(date: String): Flow<WaterIntakeEntity?> {
        return waterIntakeDao.getWaterIntakeForDate(date)
    }

    override suspend fun getWaterIntakeForDateOnce(date: String): WaterIntakeEntity? {
        return waterIntakeDao.getWaterIntakeForDateOnce(date)
    }

    override fun getWaterIntakeForRange(startDate: String, endDate: String): Flow<List<WaterIntakeEntity>> {
        return waterIntakeDao.getWaterIntakeForRange(startDate, endDate)
    }

    override suspend fun addWater(date: String, amount: Int) {
        val existing = waterIntakeDao.getWaterIntakeForDateOnce(date)
        if (existing != null) {
            waterIntakeDao.addWater(date, amount)
        } else {
            waterIntakeDao.insertOrUpdate(
                WaterIntakeEntity(
                    date = date,
                    totalMl = amount,
                    goalMl = 2000
                )
            )
        }
    }

    override suspend fun setGoal(date: String, goalMl: Int) {
        val existing = waterIntakeDao.getWaterIntakeForDateOnce(date)
        if (existing != null) {
            waterIntakeDao.insertOrUpdate(existing.copy(goalMl = goalMl))
        } else {
            waterIntakeDao.insertOrUpdate(
                WaterIntakeEntity(
                    date = date,
                    totalMl = 0,
                    goalMl = goalMl
                )
            )
        }
    }
}