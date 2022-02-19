package com.printoverit.cookbookupdate.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.printoverit.cookbookupdate.R
import com.printoverit.cookbookupdate.models.ExtendedIngredient
import com.printoverit.cookbookupdate.utils.Constaints.Companion.BASE_IMAGE_URL
import com.printoverit.cookbookupdate.utils.RecipiesDiffUtils
import kotlinx.android.synthetic.main.ingredients_row_item.view.*

class IngredientsAdapter :RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>(){

   private  var ingredientsList= emptyList<ExtendedIngredient>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
      return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ingredients_row_item,parent,false))
    }

    override fun onBindViewHolder(holder: IngredientsAdapter.MyViewHolder, position: Int) {
        holder.itemView.ingredients_Imageview.load(BASE_IMAGE_URL+ingredientsList[position].image){
            crossfade(600)
            error(R.drawable.ic_no_internet_image)
        }
        holder.itemView.ingredints_title.text=ingredientsList[position].name
        holder.itemView.ingredints_desction.text=ingredientsList[position].amount.toString()
        holder.itemView.ingredints_grmas.text=ingredientsList[position].unit.toString()
        holder.itemView.ingredints_extra.text=ingredientsList[position].consistency.toString()
        holder.itemView.ingredints_fun.text=ingredientsList[position].original.toString()
    }

    override fun getItemCount(): Int {
      return ingredientsList.size
    }
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }
    fun setData(Newingredients: List<ExtendedIngredient>){
        val IngredientsDiffUtil=RecipiesDiffUtils(
            ingredientsList,
            Newingredients
        )
        val DiffUtilResult= DiffUtil.calculateDiff(IngredientsDiffUtil)
        ingredientsList=Newingredients
        DiffUtilResult.dispatchUpdatesTo(this)
    }
}