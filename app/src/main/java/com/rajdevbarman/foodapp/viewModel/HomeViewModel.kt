package com.rajdevbarman.foodapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rajdevbarman.foodapp.db.MealDatabase
import com.rajdevbarman.foodapp.pojo.*
import com.rajdevbarman.foodapp.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase: MealDatabase
):ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularMealLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoryLiveData = MutableLiveData<List<Category>>()
    private var favoriteMealLiveData = mealDatabase.mealDao().getAllMeal()
    private var bottomSheetMealLiveData = MutableLiveData<Meal>()

    fun getRandomMeal(){
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body() != null){
                    val randomMeal: Meal = response.body()!!.meals[0]
                    Log.d("Test", "Meal ID: ${randomMeal.idMeal} - Meal Name: ${randomMeal.strMeal}")
                    randomMealLiveData.value = randomMeal
                } else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("homeFragment", t.message.toString())
            }

        })
    }

    fun getPopularItems(){
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object : Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if (response.body() != null){
                    popularMealLiveData.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }

        })
    }

    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object: Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let { categoryList ->
                    categoryLiveData.postValue(categoryList.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }

        })
    }

    fun getMealById(id: String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let { meal ->
                    bottomSheetMealLiveData.postValue(meal)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }

        })
    }

    fun deleteMeal(meal: Meal){
        viewModelScope.launch(Dispatchers.IO) {
            mealDatabase.mealDao().delete(meal)
        }
    }

    fun insertMeal(meal: Meal){
        viewModelScope.launch(Dispatchers.IO) {
            mealDatabase.mealDao().upsert(meal)
            with(Dispatchers.Main){}
        }
    }

    fun observeRandomMealLiveData():LiveData<Meal>{
        return randomMealLiveData
    }

    fun observePopularItemLiveData(): LiveData<List<MealsByCategory>>{
        return popularMealLiveData
    }

    fun observeCategoriesLivaData(): LiveData<List<Category>>{
        return categoryLiveData
    }

    fun observeFavoritesMealsLiveData(): LiveData<List<Meal>>{
        return favoriteMealLiveData
    }

    fun observeBottomSheetLiveData(): LiveData<Meal> = bottomSheetMealLiveData

}
