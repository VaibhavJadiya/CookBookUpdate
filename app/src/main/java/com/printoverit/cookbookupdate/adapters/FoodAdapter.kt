package com.printoverit.cookbookupdate.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.printoverit.cookbookupdate.databinding.ReciepeRowItemBinding
import com.printoverit.cookbookupdate.models.FoodRecipeJson
import com.printoverit.cookbookupdate.models.Result
import com.printoverit.cookbookupdate.utils.RecipiesDiffUtils

class FoodAdapter : RecyclerView.Adapter<FoodAdapter.MyViewHolder>() {
    private var recipe = emptyList<Result>()
    class MyViewHolder(private val reciepeRowItemBinding: ReciepeRowItemBinding) :
        RecyclerView.ViewHolder(reciepeRowItemBinding.root) {

            fun bind(result : Result){
               reciepeRowItemBinding.result=result
                reciepeRowItemBinding.executePendingBindings()
            }
        companion object{
            fun from(parent: ViewGroup):MyViewHolder{
                val layoutInflater= LayoutInflater.from(parent.context)
                val binding = ReciepeRowItemBinding.inflate(layoutInflater,parent,false)
                return MyViewHolder(binding)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentRecipe=recipe[position]
        holder.bind(currentRecipe)
    }

    override fun getItemCount(): Int {
        return recipe.size
    }
    fun setData(newData:FoodRecipeJson){
        val recipeUtils=RecipiesDiffUtils(recipe,newData.results)
        val diffUtilResults=DiffUtil.calculateDiff(recipeUtils)
        recipe=newData.results
        diffUtilResults.dispatchUpdatesTo(this)

    }
}