package com.rajdevbarman.foodapp.retrofit

import com.rajdevbarman.foodapp.pojo.MealList
import retrofit2.Call
import retrofit2.http.GET

interface MealApi {

    @GET("random.php")
    fun getRandomMeal():Call<MealList>;
}