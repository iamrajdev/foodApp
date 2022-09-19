package com.rajdevbarman.foodapp.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rajdevbarman.foodapp.pojo.Meal
import com.rajdevbarman.foodapp.pojo.MealList
import com.rajdevbarman.foodapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel: ViewModel() {
    private var searchedMealLiveData = MutableLiveData<Meal>()

    fun searchMealDetails(name: String, context: Context?) {
        RetrofitInstance.api.getMealByName(name).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body()?.meals == null){
                    Toast.makeText(context?.applicationContext, "No such a meal", Toast.LENGTH_SHORT).show()
                } else {
                    searchedMealLiveData.value = response.body()!!.meals[0]
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("searchedViewModel", t.message.toString())
            }

        })
    }

   fun observeSearchedLiveData(): LiveData<Meal>{
       return searchedMealLiveData
   }
}