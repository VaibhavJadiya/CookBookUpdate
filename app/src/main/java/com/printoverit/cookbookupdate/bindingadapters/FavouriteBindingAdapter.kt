package com.printoverit.cookbookupdate.bindingadapters

import android.view.View
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.printoverit.cookbookupdate.adapters.FavouriteRecipeAdapter
import com.printoverit.cookbookupdate.room.FavouriteEntity

class FavouriteBindingAdapter {

    companion object{
        @BindingAdapter("viewVisibility","setData", requireAll = false)
        @JvmStatic
        fun setDateAndVieVisibility(
            view: View,
            favouriteEntity:List<FavouriteEntity>?,
            mAdapter: FavouriteRecipeAdapter?
        ){
            if(favouriteEntity.isNullOrEmpty()){
                when(view){
                    is ImageView->{
                        view.visibility=View.VISIBLE
                    }
                    is TextView->{
                        view.visibility=View.VISIBLE
                    }
                    is RecyclerView->{
                        view.visibility=View.INVISIBLE
                    }
                }
            }else{
                when(view){
                    is ImageView->{
                        view.visibility=View.INVISIBLE
                    }
                    is TextView->{
                        view.visibility=View.INVISIBLE
                    }
                    is RecyclerView->{
                        view.visibility=View.VISIBLE
                        mAdapter?.setData(favouriteEntity)
                    }
                }

            }

        }
    }
}