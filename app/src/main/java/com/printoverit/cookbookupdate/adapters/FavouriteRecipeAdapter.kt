package com.printoverit.cookbookupdate.adapters

import android.view.*
import androidx.core.content.ContextCompat

import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.printoverit.cookbookupdate.FavouraiteReciepeFragmentDirections
import com.printoverit.cookbookupdate.R
import com.printoverit.cookbookupdate.databinding.FavouriteRecipeItemBinding
import com.printoverit.cookbookupdate.databinding.ReciepeRowItemBinding
import com.printoverit.cookbookupdate.room.FavouriteEntity
import com.printoverit.cookbookupdate.utils.RecipiesDiffUtils
import com.printoverit.cookbookupdate.viewmodel.FoodViewModel
import kotlinx.android.synthetic.main.favourite_recipe_item.view.*

class FavouriteRecipeAdapter (private val requireActivity:FragmentActivity
,private val foodViewModel: FoodViewModel)
    : RecyclerView.Adapter<FavouriteRecipeAdapter.MyViewHolder>() , ActionMode.Callback {
    var multiSelected=false
    private lateinit var mActionmode: ActionMode
    private lateinit var rootView: View
    var seleectRecipeis= arrayListOf<FavouriteEntity>()
    val myviewHolders= arrayListOf<MyViewHolder>()
    var facouriteRecipie= emptyList<FavouriteEntity>()
    //Either we have to
    class MyViewHolder(private val binding:FavouriteRecipeItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(favouriteEntity: FavouriteEntity){
            binding.favouriteRecipe=favouriteEntity
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent:ViewGroup):MyViewHolder{
                val layoutInflater= LayoutInflater.from(parent.context)
                val binding = FavouriteRecipeItemBinding.inflate(layoutInflater,parent,false)
                return FavouriteRecipeAdapter.MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        myviewHolders.add(holder)
        rootView=holder.itemView.rootView
        var selectedRecipie=facouriteRecipie[position]
        holder.bind(selectedRecipie)

        holder.itemView.favouriterecipeRowLayout.setOnClickListener {
            if (multiSelected){
                applySelection(holder,selectedRecipie)
            }
            else{
                val action=FavouraiteReciepeFragmentDirections.actionFavouraiteReciepeFragmentToDetailsActivity(selectedRecipie.result)
                holder.itemView.findNavController().navigate(action)
            }
        }
        holder.itemView.favouriterecipeRowLayout.setOnLongClickListener {
            if (!multiSelected){
                multiSelected=true
                requireActivity.startActionMode(this)
                applySelection(holder,selectedRecipie)
                true
            }else{
                multiSelected=false
                false
            }
        }
    }

    override fun getItemCount(): Int {
        return  facouriteRecipie.size
    }

    private fun changeRecipeStyle(holder:MyViewHolder, backgroundColor:Int,strokeColor:Int){
        holder.itemView.favouriterecipeRowLayout.setBackgroundColor(ContextCompat.getColor(requireActivity,backgroundColor))
        holder.itemView.favouriteRecipeCarview.setStrokeColor(ContextCompat.getColor(requireActivity,strokeColor))
    }



    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
      mode?.menuInflater?.inflate(R.menu.favourite_contextctual_menu,menu)
        mActionmode=mode!!
        return  true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return  true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId==R.id.context_menu_delete){
            seleectRecipeis.forEach {
                foodViewModel.deleteFavouriteRecipe(it)
            }
            showSnackBar("${seleectRecipeis.size} Recipes Deleted")
            multiSelected=false
            seleectRecipeis.clear()
            mode?.finish()
        }
       return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        myviewHolders.forEach{holders->
            changeRecipeStyle(holders,R.color.white,R.color.grey)

        }
        multiSelected=false
        seleectRecipeis.clear()
    }

    private fun applySelection(holder:MyViewHolder,curentRecipe:FavouriteEntity){
        if(seleectRecipeis.contains(curentRecipe)){
            seleectRecipeis.remove(curentRecipe)
            changeRecipeStyle(holder,R.color.grey,R.color.purple_500)
            applyActionModeTittle()
        }else{
            seleectRecipeis.add(curentRecipe)
            changeRecipeStyle(holder,R.color.white,R.color.grey)
        }
    }

    fun setData(newFavoriteRecipes:List<FavouriteEntity>){
        val favouriteRecipeDiffUitls= RecipiesDiffUtils(facouriteRecipie,newFavoriteRecipes)
        val diffUtilResult=DiffUtil.calculateDiff(favouriteRecipeDiffUitls)
        facouriteRecipie=newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun applyActionModeTittle(){
        when(seleectRecipeis.size){
            0->{
                mActionmode.finish()
            }
            1->{
                mActionmode.title="${seleectRecipeis.size} Item Selected"
            }
            else->{
                mActionmode.title="${seleectRecipeis.size} Items Selected"
            }
        }
    }

    private fun showSnackBar(messsgae:String){
        Snackbar.make(rootView,messsgae, Snackbar.LENGTH_SHORT).setAction("Done"){}.show()
    }
     fun clearContextualActionMode(){
         if(this::mActionmode.isInitialized){
             mActionmode?.finish()
         }
     }
}
