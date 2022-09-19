package com.rajdevbarman.foodapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.rajdevbarman.foodapp.R
import com.rajdevbarman.foodapp.activities.MealActivity
import com.rajdevbarman.foodapp.databinding.FragmentSearchBinding
import com.rajdevbarman.foodapp.fragments.HomeFragment.Companion.MEAL_ID
import com.rajdevbarman.foodapp.fragments.HomeFragment.Companion.MEAL_NAME
import com.rajdevbarman.foodapp.fragments.HomeFragment.Companion.MEAL_THUMB
import com.rajdevbarman.foodapp.pojo.Meal
import com.rajdevbarman.foodapp.viewModel.SearchViewModel


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchViewModel: SearchViewModel
    private var mealId = ""
    private var mealStr = ""
    private var mealThumb = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onSearchClick()
        observeSearchedLiveData()
        setOnMealCardClicked()
    }

    private fun setOnMealCardClicked() {
        binding.cardSearchedMeal.setOnClickListener {
            val intent = Intent(context, MealActivity::class.java)
            intent.putExtra(MEAL_ID, mealId)
            intent.putExtra(MEAL_NAME, mealStr)
            intent.putExtra(MEAL_THUMB, mealThumb)
            startActivity(intent)
        }
    }

    private fun observeSearchedLiveData() {
        searchViewModel.observeSearchedLiveData().observe(viewLifecycleOwner
        ) { meal ->
            if (meal == null) {
                Toast.makeText(context, "No such a meal", Toast.LENGTH_SHORT).show()
            } else {
                binding.apply {
                    mealId = meal.idMeal
                    mealStr = meal.strMeal!!
                    mealThumb = meal.strMealThumb!!

                    Glide.with(requireContext().applicationContext)
                        .load(meal.strMealThumb)
                        .into(imgSearchMeal)

                    tvSearchedMeal.text = meal.strMeal
                    cardSearchedMeal.visibility = View.VISIBLE

                }
            }
        }
    }

    private fun onSearchClick() {
        binding.edSearch.setOnClickListener {
            searchViewModel.searchMealDetails(binding.edSearch.text.toString(), context)
        }
    }

}