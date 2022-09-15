package com.rajdevbarman.foodapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rajdevbarman.foodapp.pojo.Meal

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(meal: Meal)

    @Delete
    suspend fun delete(meal: Meal)

    @Query("SELECT * FROM meal_db")
    fun getAllMeal(): LiveData<List<Meal>>

}