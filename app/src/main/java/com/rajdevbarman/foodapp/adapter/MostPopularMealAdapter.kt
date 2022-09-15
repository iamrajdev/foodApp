package com.rajdevbarman.foodapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rajdevbarman.foodapp.databinding.PopularItemsBinding
import com.rajdevbarman.foodapp.pojo.MealsByCategory

class MostPopularMealAdapter: RecyclerView.Adapter<MostPopularMealAdapter.PopularMealViewHolder>() {

    lateinit var onItemClick:((MealsByCategory) -> Unit)
    var onLongItemClicked: ((MealsByCategory) -> Unit)? = null
    private var mealList= ArrayList<MealsByCategory>()

    fun setMeals(mealList: ArrayList<MealsByCategory>){
        this.mealList = mealList
        notifyDataSetChanged()
    }

    class PopularMealViewHolder( val binding: PopularItemsBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(PopularItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealList[position].strMealThumb)
            .into(holder.binding.imgPopularMealItem)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealList[position])
        }

        holder.itemView.setOnLongClickListener {
            onLongItemClicked?.invoke(mealList[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }
}