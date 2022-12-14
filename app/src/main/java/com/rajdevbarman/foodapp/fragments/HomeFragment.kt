package com.rajdevbarman.foodapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.rajdevbarman.foodapp.R
import com.rajdevbarman.foodapp.activities.CategoryMealsActivity
import com.rajdevbarman.foodapp.activities.MainActivity
import com.rajdevbarman.foodapp.activities.MealActivity
import com.rajdevbarman.foodapp.adapter.CategoriesAdapter
import com.rajdevbarman.foodapp.adapter.MostPopularMealAdapter
import com.rajdevbarman.foodapp.databinding.FragmentHomeBinding
import com.rajdevbarman.foodapp.fragments.bottomsheet.MealBottomSheetFragment
import com.rajdevbarman.foodapp.pojo.MealsByCategory
import com.rajdevbarman.foodapp.pojo.Meal
import com.rajdevbarman.foodapp.viewModel.HomeViewModel


class HomeFragment : Fragment() {

    private lateinit var binding:FragmentHomeBinding
    private lateinit var viewModel:HomeViewModel
    private lateinit var randomMeal:Meal
    private lateinit var popularItemAdapter: MostPopularMealAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object {
        const val MEAL_ID = "com.rajdevbarman.foodapp.fragments.idMeal"
        const val MEAL_NAME = "com.rajdevbarman.foodapp.fragments.nameMeal"
        const val MEAL_THUMB = "com.rajdevbarman.foodapp.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.rajdevbarman.foodapp.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentHomeBinding.inflate(layoutInflater)

        viewModel = (activity as MainActivity).viewModel
        popularItemAdapter = MostPopularMealAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoadingCase()

        popularItemRecyclerView()

        viewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItems()
        observePopularItemLiveData()

        onPopularItemClick()

        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observeCategoriesLiveData()
        onCategoryClick()

        onPopularItemLongClick()

        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homefrag_to_searchfrag)
        }

    }

    private fun showLoadingCase() {
        binding.apply {
            linearHeader.visibility = View.INVISIBLE

            tvWouldYouLikeToEat.visibility = View.INVISIBLE
            randomMealCard.visibility = View.INVISIBLE
            tvOverPopular.visibility = View.INVISIBLE
            recViewMealsPopular.visibility = View.INVISIBLE
            tvCategories.visibility = View.INVISIBLE
            categoryCard.visibility = View.INVISIBLE
            loadingGif.visibility = View.VISIBLE
            rootHome.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.g_loading))

        }
    }

    private fun cancelLoadingCase() {
        binding.apply {
            linearHeader.visibility = View.VISIBLE
            tvWouldYouLikeToEat.visibility = View.VISIBLE
            randomMealCard.visibility = View.VISIBLE
            tvOverPopular.visibility = View.VISIBLE
            recViewMealsPopular.visibility = View.VISIBLE
            tvCategories.visibility = View.VISIBLE
            categoryCard.visibility = View.VISIBLE
            loadingGif.visibility = View.INVISIBLE
            rootHome.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))

        }
    }

    private fun onPopularItemLongClick() {
        popularItemAdapter.onLongItemClicked = {meal ->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager, "Meal Info")
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.recViewCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }

    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLivaData().observe(viewLifecycleOwner, Observer { categories ->

            cancelLoadingCase()

            categoriesAdapter.setCategoriesList(categories)
        })
    }

    private fun onPopularItemClick() {
        popularItemAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun popularItemRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemAdapter
        }
    }

    private fun observePopularItemLiveData() {
        viewModel.observePopularItemLiveData().observe(viewLifecycleOwner) {
            mealList ->
            popularItemAdapter.setMeals(mealList = mealList as ArrayList<MealsByCategory>)
        }
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner
        ) { meal ->
            Glide.with(this@HomeFragment)
                .load(meal!!.strMealThumb)
                .into(binding.imgRandomMeal)
            this.randomMeal = meal
        }
    }
}