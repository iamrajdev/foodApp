package com.rajdevbarman.foodapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rajdevbarman.foodapp.pojo.MealsByCategory
import com.rajdevbarman.foodapp.pojo.MealsByCategoryList
import com.rajdevbarman.foodapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class CategoryMealsViewModel:ViewModel() {
    val mealsLiveData = MutableLiveData<List<MealsByCategory>>()

    fun getMealsByCategory(categoryName: String){
        RetrofitInstance.api.getMealsByCategory(categoryName).enqueue(object : Callback<MealsByCategoryList>{
            override fun onResponse(
                call: Call<MealsByCategoryList>,
                response: Response<MealsByCategoryList>
            ) {
                response.body()?.let { mealList ->
                    mealsLiveData.postValue(mealList.meals)
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("CategoryMealsViewModel", t.message.toString())
            }
        })
    }

    fun observeMealsLiveData(): LiveData<List<MealsByCategory>>{
        return mealsLiveData
    }
}
